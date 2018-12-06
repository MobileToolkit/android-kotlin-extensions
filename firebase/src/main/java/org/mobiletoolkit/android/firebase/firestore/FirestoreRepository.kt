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

    val entityClazz: Class<Entity>  //TODO: make this automatic
//    fun <T: Entity> buildEntity(documentSnapshot: DocumentSnapshot?, ofClass: Class<T>): T? =
//        documentSnapshot?.toObjectWithReference(ofClass)
//
//    inline fun <reified T : Entity> buildEntity(documentSnapshot: DocumentSnapshot?): T? = buildEntity(documentSnapshot, T::class.java)

    val collectionPath: String

    val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    val collectionReference: CollectionReference
        get() = db.collection(collectionPath)

    override fun exists(identifier: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "exists -> collectionPath: $collectionPath | identifier: $identifier")

        documentExists(identifier).addOnCompleteListener(onCompleteListener)
    }

    override fun get(identifier: String, onCompleteListener: OnCompleteListener<Entity?>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath | identifier: $identifier")

        getDocument(identifier).addOnCompleteListener(onCompleteListener)
    }

    fun get(identifier: String, callback: RepositoryEventCallback<Entity>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath | identifier: $identifier")

        collectionReference.document(identifier).addSnapshotListener { documentSnapshot, exception ->
            callback(documentSnapshot?.toObjectWithReference(entityClazz), exception)
        }
    }

    override fun create(entity: Entity, identifier: String?, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "create -> collectionPath: $collectionPath | entity: $entity | identifier: $identifier")

        createDocument(entity, identifier).addOnCompleteListener(onCompleteListener)
    }

    override fun create(vararg entities: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "create -> collectionPath: $collectionPath | entities: $entities")

        create(entities.toList(), onCompleteListener = onCompleteListener)
    }

    override fun create(
        entities: List<Entity>,
        identifiers: List<String?>?,
        onCompleteListener: OnCompleteListener<Boolean>
    ) {
        Log.v(TAG, "create -> collectionPath: $collectionPath | entities: $entities | identifiers: $identifiers")

        createDocuments(entities, identifiers).addOnCompleteListener(onCompleteListener)
    }

    override fun update(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "update -> collectionPath: $collectionPath | entity: $entity")

        updateDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun update(vararg entities: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "update -> collectionPath: $collectionPath | entities: $entities")

        updateDocuments(entities.toList()).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> collectionPath: $collectionPath | entity: $entity")

        deleteDocument(entity).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(identifier: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> collectionPath: $collectionPath | identifier: $identifier")

        deleteDocument(identifier).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(vararg entities: Entity, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> collectionPath: $collectionPath | entities: $entities")

        deleteDocuments(entities.toList()).addOnCompleteListener(onCompleteListener)
    }

    override fun delete(vararg identifiers: String, onCompleteListener: OnCompleteListener<Boolean>) {
        Log.v(TAG, "delete -> collectionPath: $collectionPath | identifiers: $identifiers")

        deleteDocuments(identifiers = identifiers.toList()).addOnCompleteListener(onCompleteListener)
    }

    override fun get(onCompleteListener: OnCompleteListener<List<Entity>>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath")

        getDocuments().addOnCompleteListener(onCompleteListener)
    }

    fun get(callback: RepositoryEventCallback<List<Entity>>) {
        Log.v(TAG, "get -> collectionPath: $collectionPath")

        collectionReference.addSnapshotListener { querySnapshot, exception ->
            callback(querySnapshot?.mapNotNull { it.toObjectWithReference(entityClazz) } ?: listOf(),
                exception)
        }
    }

    fun documentExists(identifier: String): Task<Boolean> {
        Log.v(TAG, "documentExists -> collectionPath: $collectionPath | identifier: $identifier")

        return collectionReference.document(identifier).get().continueWith {
            it.result?.exists() == true
        }
    }

    fun getDocument(identifier: String): Task<Entity?> {
        Log.v(TAG, "getDocument -> collectionPath: $collectionPath | identifier: $identifier")

        return collectionReference.document(identifier).get().continueWith {
            it.result?.toObjectWithReference(entityClazz)
        }
    }

    fun createDocument(entity: Entity, identifier: String? = null): Task<Boolean> {
        Log.v(TAG, "createDocument -> collectionPath: $collectionPath | entity: $entity | identifier: $identifier")

        return with(collectionReference) {
            (identifier?.let { docId ->
                document(docId)
            } ?: document()).set(entity).continueWith {
                it.isSuccessful
            }
        }
    }

    fun createDocuments(entities: List<Entity>, identifiers: List<String?>? = null): Task<Boolean> {
        Log.v(
            TAG,
            "createDocuments -> collectionPath: $collectionPath | entities: $entities | identifier: $identifiers"
        )

        //TODO - split into batches of 20

        val batch = db.batch()

        entities.forEachIndexed { index, entity ->
            val docRef = with(collectionReference) {
                (identifiers?.get(index)?.let { docId ->
                    document(docId)
                } ?: document())
            }

            batch.set(docRef, entity)
        }

        return batch.commit().continueWith {
            it.isSuccessful
        }
    }

    fun updateDocument(entity: Entity): Task<Boolean> {
        Log.v(TAG, "updateDocument -> collectionPath: $collectionPath | entity: $entity")

        entity.identifier?.let { identifier ->
            return collectionReference.document(identifier).set(entity, SetOptions.merge()).continueWith {
                it.isSuccessful
            }
        }
    }

    fun updateDocuments(entities: List<Entity>): Task<Boolean> {
        Log.v(TAG, "updateDocuments -> collectionPath: $collectionPath | entities: $entities")

        //TODO - split into batches of 20

        val batch = db.batch()

        entities.forEach { entity ->
            entity.documentReference?.let { docRef ->
                batch.set(docRef, entity, SetOptions.merge())
            }
        }

        return batch.commit().continueWith {
            it.isSuccessful
        }
    }

    fun deleteDocument(entity: Entity): Task<Boolean> {
        Log.v(TAG, "deleteDocument -> collectionPath: $collectionPath | entity: $entity")

        entity.identifier?.let { identifier ->
            return deleteDocument(identifier)
        }
    }

    fun deleteDocument(identifier: String): Task<Boolean> {
        Log.v(TAG, "deleteDocument -> collectionPath: $collectionPath | identifier: $identifier")

        return collectionReference.document(identifier).delete().continueWith {
            it.isSuccessful
        }
    }

    fun deleteDocuments(entities: List<Entity>? = null, identifiers: List<String?>? = null): Task<Boolean> {
        Log.v(
            TAG,
            "deleteDocuments -> collectionPath: $collectionPath | entities: $entities | identifiers: $identifiers"
        )

        //TODO - split into batches of 20

        val batch = db.batch()

        entities?.forEach { entity ->
            entity.documentReference?.let { docRef ->
                batch.delete(docRef)
            }
        }

        identifiers?.filterNotNull()?.forEach { identifier ->
            batch.delete(collectionReference.document(identifier))
        }

        return batch.commit().continueWith {
            it.isSuccessful
        }
    }

    fun getDocuments(): Task<List<Entity>> {
        Log.v(TAG, "getDocuments -> collectionPath: $collectionPath")

        return collectionReference.get().continueWith { task ->
            task.result?.mapNotNull { it.toObjectWithReference(entityClazz) } ?: listOf()
        }
    }
}

typealias RepositoryEventCallback<T> = (data: T?, exception: Exception?) -> Unit