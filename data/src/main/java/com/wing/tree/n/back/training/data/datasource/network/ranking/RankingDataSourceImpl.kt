package com.wing.tree.n.back.training.data.datasource.network.ranking

import androidx.annotation.MainThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.wing.tree.n.back.training.data.model.Ranking
import javax.inject.Inject

class RankingDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : RankingDataSource {
    private val collectionReference = firebaseFirestore.collection(COLLECTION_PATH)
    private val queryCursorArray = arrayOfNulls<DocumentSnapshot>(5)

    override suspend fun getRankingList(
        page: Int,
        pageSize: Long,
        @MainThread
        onSuccess: (List<Ranking>) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        if (page == 0) {
            collectionReference.limit(pageSize)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.count() >= pageSize) {
                        queryCursorArray[1] = querySnapshot.last()
                    }

                    onSuccess(querySnapshot.mapNotNull(DocumentSnapshot::toObject))
                }.addOnFailureListener {
                    onFailure(it)
                }
        } else {
            val queryCursor = queryCursorArray[page] ?: return

            collectionReference.limit(pageSize)
                .startAfter(queryCursor)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.count() >= pageSize) {
                        if (page < 4) {
                            queryCursorArray[page.inc()] = querySnapshot.last()
                        }
                    }

                    onSuccess(querySnapshot.mapNotNull(DocumentSnapshot::toObject))
                }.addOnFailureListener {
                    onFailure(it)
                }
        }
    }

    override suspend fun registerRanking(
        ranking: Ranking,
        @MainThread
        onSuccess: (Int) -> Unit,
        @MainThread
        onFailure: (Exception) -> Unit
    ) {
        collectionReference.limit(50)
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

                            return@with
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

        firebaseFirestore.runTransaction { transaction ->
            if (count >= 50) {
                documents.last()?.reference?.let {
                    transaction.delete(it)
                }
            }

            transaction.set(documentReference, ranking)
        }.addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    companion object {
        private const val COLLECTION_PATH = "ranking"
    }
}