package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.wing.tree.n.back.training.data.constant.long
import com.wing.tree.n.back.training.data.model.Ranking
import javax.inject.Inject
import kotlin.NullPointerException

class RankingDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : RankingDataSource {
    private val collectionReference = firebaseFirestore.collection(COLLECTION_PATH)
    private val queryCursorArray = arrayOfNulls<DocumentSnapshot>(5)

    override suspend fun checkRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: (Boolean) -> Unit,
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
                        with(querySnapshot.last()) {
                            onSuccess(ranking.isHigher(this.toObject()))
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
        onSuccess: (Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        collectionReference.limit(50)
            .orderBy(Field.N, Query.Direction.DESCENDING)
            .orderBy(Field.ROUNDS, Query.Direction.DESCENDING)
            .orderBy(Field.ELAPSED_TIME)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents

                if (querySnapshot.size() < 1) {
                    updateRanking(
                        documents,
                        ranking, {
                            onSuccess(0)
                        },
                        onFailure
                    )

                    return@addOnSuccessListener
                }

                run {
                    querySnapshot.forEachIndexed { index, queryDocumentSnapshot ->
                        with(queryDocumentSnapshot.toObject<Ranking>()) {
                            if (isHigher(ranking)) {
                                updateRanking(
                                    documents,
                                    ranking, {
                                        onSuccess(index)
                                    },
                                    onFailure
                                )

                                return@run
                            }
                        }
                    }
                }
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