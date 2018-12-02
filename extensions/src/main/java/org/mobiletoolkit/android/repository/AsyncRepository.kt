package org.mobiletoolkit.android.repository

import com.google.android.gms.tasks.OnCompleteListener

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface AsyncRepository<Entity : Model<Identifier>, Identifier> : Repository<Entity, Identifier> {

    fun exists(identifier: Identifier, onCompleteListener: OnCompleteListener<Boolean>)

    fun get(identifier: Identifier, onCompleteListener: OnCompleteListener<Entity?>)

    fun create(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)

    fun update(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)

    fun delete(entity: Entity, onCompleteListener: OnCompleteListener<Boolean>)
    fun delete(identifier: Identifier, onCompleteListener: OnCompleteListener<Boolean>)

    fun get(onCompleteListener: OnCompleteListener<List<Entity>>)
}