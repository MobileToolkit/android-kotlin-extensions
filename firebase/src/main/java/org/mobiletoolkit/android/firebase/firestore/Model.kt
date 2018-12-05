package org.mobiletoolkit.android.firebase.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import org.mobiletoolkit.android.repository.Model

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface HasDocumentReference {
    var documentReference: DocumentReference?
}

@IgnoreExtraProperties
abstract class Model : Model<String>, HasDocumentReference {

    @get:Exclude
    override var documentReference: DocumentReference? = null

    @get:Exclude
    override val identifier: String?
        get() = documentReference?.id
}

fun <Entity : HasDocumentReference> DocumentSnapshot.toObjectWithReference(valueType: Class<Entity>): Entity? =
    toObject(valueType)?.let {
        it.documentReference = reference

        it
    }
