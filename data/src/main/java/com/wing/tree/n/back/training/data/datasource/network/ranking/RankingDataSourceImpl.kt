package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.wing.tree.n.back.training.data.constant.long
import com.wing.tree.n.back.training.data.model.Ranking
import com.wing.tree.n.back.training.domain.model.RankCheckParameter
import javax.inject.Inject
import kotlin.NullPointerException

class RankingDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : RankingDataSource {
    private val collectionReference = firebaseFirestore.collection(COLLECTION_PATH)
    private val queryCursorArray = arrayOfNulls<DocumentSnapshot>(5)

    override suspend fun checkRanking(
        rankCheckParameter: RankCheckParameter,
        @MainThread
        onSuccess: (Boolean, Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        collectionReference
            .orderBy(Field.N, Query.Direction.DESCENDING)
            .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
            .orderBy(Field.ELAPSED_TIME)
            .limit(LOWEST_RANK.long)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { querySnapshot ->
                        val count = querySnapshot.count()

                        run {
                            querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                                with(queryDocumentSnapshot.toObject<Ranking>()) {
                                    println("check idx:$index ranking:$this, rankcheckparam:${rankCheckParameter}")
                                    if (isHigher(rankCheckParameter)) {
                                        println("check idx:$index ranking:$this, 노진입??? 아니면 나의 실수.")
                                        onSuccess(true, index)

                                        return@run
                                    }
                                }
                            }

                            if (count < LOWEST_RANK) {
                                onSuccess(true, count.inc())
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

    override suspend fun getRankingList(
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
            val queryCursor = queryCursorArray[page] ?: return

            collectionReference
                .orderBy(Field.N, Query.Direction.DESCENDING)
                .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
                .orderBy(Field.ELAPSED_TIME)
                .startAfter(queryCursor)
                .limit(pageSize)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { querySnapshot ->
                            if (querySnapshot.count() >= pageSize) {
                                if (page < 4) {
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
        collectionReference.limit(LOWEST_RANK.long)
            .orderBy(Field.N, Query.Direction.DESCENDING)
            .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
            .orderBy(Field.ELAPSED_TIME)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents

                updateRanking(
                    documents,
                    ranking,
                    onSuccess,
                    onFailure
                )
            }
            .addOnFailureListener {
                onFailure.invoke(it)
            }
    }

    private fun updateRanking(
        documents: List<DocumentSnapshot?>,
        ranking: Ranking,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val count = documents.count()
        val documentReference = collectionReference.document()
        val id = documentReference.id

        ranking.id = id

        firebaseFirestore.runTransaction { transaction ->
            if (count >= LOWEST_RANK) {
                if (documents.isNotEmpty()) {
                    documents.last()?.reference?.let {
                        transaction.delete(it)
                    }
                }
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
    }
}