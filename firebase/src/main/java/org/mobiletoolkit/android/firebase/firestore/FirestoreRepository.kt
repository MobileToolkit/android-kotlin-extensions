package org.mobiletoolkit.android.firebase.firestore

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.mobiletoolkit.android.repository.AsyncRepository

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface FirestoreRepository<Entity : Model> : AsyncRepository<Entity, String> {

    companion object {
        private const val TAG = "FirestoreRepository"
    }

    val entityClazz: Class<Entity>

    val collectionPath: String

    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val collectionReference: CollectionReference
        get() = db.collection(collectionPath)

    override fun exists(identifier: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "exists -> identifier: $identifier | collectionPath: $collectionPath")

        documentExists(identifier).addOnCompleteListener(onCompleteListener)
    }

    override fun get(identifier: String, onCompleteListener: OnCompleteListener<Entity?>) {
        Log.v(TAG, "get -> identifier: $identifier | collectionPath: $collectionPath")

        getDocument(identifier).addOnCompleteListener(onCompleteListener)
    }

    fun get(identifier: String, listener: RepositoryListener<Entity>) {
        Log.v(TAG, "get -> identifier: $identifier | collectionPath: $collectionPath")

        collectionReference.document(identifier).addSnapshotListener { documentSnapshot, exception ->
            listener(documentSnapshot?.toObjectWithReference(entityClazz), exception)
        }
    }

    override fun create(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "create -> entity: $entity | collectionPath: $collectionPath")

        createDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun update(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "update -> entity: $entity | collectionPath: $collectionPath")

        updateDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> entity: $entity | collectionPath: $collectionPath")

        deleteDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(identifier: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> identifier: $identifier | collectionPath: $collectionPath")

        deleteDocument(identifier).addOnCompleteListener(onCompleteListener)
    }

    override fun get(onCompleteListener: OnCompleteListener<List<Entity>>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath")

        getDocuments().addOnCompleteListener(onCompleteListener)
    }

    fun get(listener: RepositoryListener<List<Entity>>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath")

        collectionReference.addSnapshotListener { querySnapshot, exception ->
            listener(querySnapshot?.mapNotNull { it.toObjectWithReference(entityClazz) } ?: listOf(), exception)
        }
    }

    private fun documentExists(identifier: String): Task<Boolean> {
        Log.v(TAG, "documentExists -> collectionPath: $collectionPath | identifier: $identifier")

        return collectionReference.document(identifier).get().continueWith {
            it.result?.exists() == true
        }
    }

    private fun getDocument(identifier: String): Task<Entity?> {
        Log.v(TAG, "getDocument -> collectionPath: $collectionPath | identifier: $identifier")

        return collectionReference.document(identifier).get().continueWith {
            it.result?.toObjectWithReference(entityClazz)
        }
    }

    private fun createDocument(entity: Entity): Task<Boolean> {
        Log.v(TAG, "createDocument -> entity: $entity | collectionPath: $collectionPath")

        return collectionReference.document().set(entity).continueWith {
            it.isSuccessful
        }
    }

    private fun updateDocument(entity: Entity): Task<Boolean> {
        Log.v(TAG, "updateDocument -> entity: $entity | collectionPath: $collectionPath")

        return collectionReference.document(entity.documentReference.id).set(entity, SetOptions.merge()).continueWith {
            it.isSuccessful
        }
    }

    private fun deleteDocument(entity: Entity): Task<Boolean> {
        Log.v(TAG, "deleteDocument -> entity: $entity | collectionPath: $collectionPath")

        return deleteDocument(entity.documentReference.id)
    }

    private fun deleteDocument(identifier: String): Task<Boolean> {
        Log.v(TAG, "deleteDocument -> identifier: $identifier | collectionPath: $collectionPath")

        return collectionReference.document(identifier).delete().continueWith {
            it.isSuccessful
        }
    }

    private fun getDocuments(): Task<List<Entity>> {
        Log.v(TAG, "getDocuments -> collectionPath: $collectionPath")

        return collectionReference.get().continueWith { task ->
            task.result?.mapNotNull { it.toObjectWithReference(entityClazz) } ?: listOf()
        }
    }
}

typealias RepositoryListener<T> = (entity: T?, exception: Exception?) -> Unit