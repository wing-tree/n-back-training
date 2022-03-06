package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.wing.tree.n.back.training.data.constant.long
import com.wing.tree.n.back.training.data.model.Ranking
import com.wing.tree.n.back.training.domain.model.RankCheckParameter
import javax.inject.Inject

class RankingDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : RankingDataSource {
    private val collectionReference = firebaseFirestore.collection(COLLECTION_PATH)
    private val queryCursorArray = arrayOfNulls<DocumentSnapshot>(PAGE_COUNT)

    private var count = 0
    private var last: DocumentSnapshot? = null

    override suspend fun checkRanking(
        rankCheckParameter: RankCheckParameter,
        @MainThread
        onSuccess: (Boolean, Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        count = 0
        last = null

        collectionReference
            .orderBy(Field.N, Query.Direction.DESCENDING)
            .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
            .orderBy(Field.ELAPSED_TIME)
            .limit(LOWEST_RANK.long)
            .get(Source.SERVER)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { querySnapshot ->
                        if (querySnapshot.isEmpty.not()) {
                            count = querySnapshot.count()
                            last = querySnapshot.last()
                        }

                        run {
                            querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                                with(queryDocumentSnapshot.toObject<Ranking>()) {
                                    if (isHigher(rankCheckParameter)) {
                                        onSuccess(true, index)

                                        return@run
                                    }
                                }
                            }

                            if (count < LOWEST_RANK) {
                                onSuccess(true, count)
                            } else {
                                onSuccess(false, 0)
                            }
                        }
                    } ?: onFailure(NullPointerException("task.result :${task.result}"))
                } else {
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    override suspend fun getRankings(
        page: Int,
        pageSize: Long,
        @MainThread
        onSuccess: (List<Ranking>) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        if (page == 0) {
            collectionReference
                .orderBy(Field.N, Query.Direction.DESCENDING)
                .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
                .orderBy(Field.ELAPSED_TIME)
                .limit(pageSize)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { querySnapshot ->
                            if (querySnapshot.count() >= pageSize) {
                                queryCursorArray[1] = querySnapshot.last()
                            }

                            onSuccess(querySnapshot.mapNotNull(DocumentSnapshot::toObject))
                        } ?: onFailure(NullPointerException("task.result :${task.result}"))
                    } else {
                        task.exception?.let { onFailure(it) }
                    }
                }
        } else {
            val queryCursor = queryCursorArray[page] ?: run {
                onSuccess(emptyList())
                return
            }

            collectionReference
                .orderBy(Field.N, Query.Direction.DESCENDING)
                .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
                .orderBy(Field.ELAPSED_TIME)
                .startAfter(queryCursor)
                .limit(pageSize)
                .get(Source.SERVER)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { querySnapshot ->
                            if (querySnapshot.count() >= pageSize) {
                                if (page < PAGE_COUNT.dec()) {
                                    queryCursorArray[page.inc()] = querySnapshot.last()
                                }
                            }

                            onSuccess(querySnapshot.mapNotNull(DocumentSnapshot::toObject))
                        } ?: onFailure(NullPointerException("task.result :${task.result}"))
                    } else {
                        task.exception?.let { onFailure(it) }
                    }
                }
        }
    }

    override suspend fun registerForRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: () -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        updateRanking(ranking, onSuccess, onFailure)
    }

    private fun updateRanking(
        ranking: Ranking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val documentReference = collectionReference.document()
        val id = documentReference.id

        ranking.id = id

        firebaseFirestore.runTransaction { transaction ->
            if (count >= LOWEST_RANK) {
                last?.let { transaction.delete(it.reference) }
            }

            transaction.set(documentReference, ranking)
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    private object Field {
        const val ELAPSED_TIME = "elapsedTime"
        const val N = "n"
        const val ROUNDS = "rounds"
    }

    companion object {
        private const val COLLECTION_PATH = "ranking"
        private const val LOWEST_RANK = 50
        private const val PAGE_COUNT = 5
    }
}