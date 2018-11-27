package org.mobiletoolkit.android.firebase.firestore

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
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

    override fun exists(key: String): Boolean {
        Log.v(TAG, "exists -> key: $key | collectionPath: $collectionPath")

        return Tasks.await(documentExists(key))
    }

    override fun get(key: String): Entity? {
        Log.v(TAG, "get -> key: $key | collectionPath: $collectionPath")

        return Tasks.await(getDocument(key))
    }

    override fun create(entity: Entity): Boolean {
        Log.v(TAG, "create -> entity: $entity | collectionPath: $collectionPath")

        return Tasks.await(createDocument(entity))
    }

    override fun update(entity: Entity): Boolean {
        Log.v(TAG, "update -> entity: $entity | collectionPath: $collectionPath")

        return Tasks.await(updateDocument(entity))
    }

    override fun delete(entity: Entity): Boolean {
        Log.v(TAG, "delete -> entity: $entity | collectionPath: $collectionPath")

        return Tasks.await(deleteDocument(entity))
    }

    override fun delete(key: String): Boolean {
        Log.v(TAG, "delete -> key: $key | collectionPath: $collectionPath")

        return Tasks.await(deleteDocument(key))
    }

    override fun get(): List<Entity> {
        Log.v(TAG, "get -> collectionPath: $collectionPath")

        return Tasks.await(getDocuments())
    }

    override fun exists(key: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "existsAsync -> key: $key | collectionPath: $collectionPath")

        documentExists(key).addOnCompleteListener(onCompleteListener)
    }

    override fun get(key: String, onCompleteListener: OnCompleteListener<Entity?>) {
        Log.v(TAG, "getAsync -> key: $key | collectionPath: $collectionPath")

        getDocument(key).addOnCompleteListener(onCompleteListener)
    }

    override fun create(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "createAsync -> entity: $entity | collectionPath: $collectionPath")

        createDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun update(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "updateAsync -> entity: $entity | collectionPath: $collectionPath")

        updateDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "deleteAsync -> entity: $entity | collectionPath: $collectionPath")

        deleteDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(key: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "deleteAsync -> key: $key | collectionPath: $collectionPath")

        deleteDocument(key).addOnCompleteListener(onCompleteListener)
    }

    override fun get(onCompleteListener: OnCompleteListener<List<Entity>>) {
        Log.v(TAG, "getAsync -> collectionPath: $collectionPath")

        getDocuments().addOnCompleteListener(onCompleteListener)
    }

    private fun documentExists(key: String): Task<Boolean> {
        Log.v(TAG, "documentExists -> collectionPath: $collectionPath | key: $key")

        return collectionReference.document(key).get().continueWith {
            it.result?.exists() == true
        }
    }

    private fun getDocument(key: String): Task<Entity?> {
        Log.v(TAG, "getDocument -> collectionPath: $collectionPath | key: $key")

        return collectionReference.document(key).get().continueWith {
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

    private fun deleteDocument(key: String): Task<Boolean> {
        Log.v(TAG, "deleteDocument -> key: $key | collectionPath: $collectionPath")

        return collectionReference.document(key).delete().continueWith {
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