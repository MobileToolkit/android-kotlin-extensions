package org.mobiletoolkit.android.repository

/**
 * Created by Sebastian Owodzin on 14/08/2018.
 */
interface Repository<Entity : Model, Key> {

    fun exists(key: Key): Boolean

    fun get(key: Key): Entity?

    fun create(entity: Entity): Boolean

    fun update(entity: Entity): Boolean

    fun delete(entity: Entity): Boolean
    fun delete(key: Key): Boolean

    fun get(): List<Entity>
}

interface Model