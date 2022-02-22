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

                println("querySnapshotsssss:${querySnapshot.size()}")
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
            println("mmmmmmmmmmmmm")
            transaction.set(documentReference, ranking)
        }.addOnSuccessListener {
            println("ssssssssss")
            onSuccess()
        }.addOnFailureListener {
            println("dddddddd")
            onFailure(it)
        }
    }

    companion object {
        private const val COLLECTION_PATH = "ranking"
    }
}