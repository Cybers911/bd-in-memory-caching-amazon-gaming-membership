package com.amazon.ata.inmemorycaching.classroom.dao;

import com.amazon.ata.inmemorycaching.classroom.dao.models.GroupMembershipCacheKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

//This will manage the calls to the actual database to the cahe manager
//The caching is done by the cache manager and the delegateDao is the actual data source
//If there is a miss in the cache,"Nodata found on the cahe" it will use the original DAO
//      to fetch the data
// the delegateDao will be called to fetch the data
//If there is  a hit in the cache, the cached data will be returned
//A cache needs a key for each data item in this case is the user id and group id
// Since we have multiple values  we will treat them as a single unit creating a new class
//holding different object thats what we call a composite key and object oriented  is a design pattern
// the values which manage and hold using the Dagger library to inject the delegateDao into the cachingDao using hashMap to map keys
//  to values, that class is named GroupMembershipCachingDao
//We will be using Dagger for dependency injection
//Also the google Guava library for caching
//We will be inserting calls to the caching DAOs between the application and the database
//We need to be suer we mimic the behavior of the original DAO, same parameters, same return type




public class GroupMembershipCachingDao {
    //Defines the reference to the  cache for the cache manager to use
    //Guava use LoadingCache objects to manage the cache,
    //  and CacheBuilder to create the cache
                    //cache-key              , value to be cached
    LoadingCache<GroupMembershipCacheKey, Boolean> theCache;

    // hace una instancia de la clase GroupMembershipDao a delegateDao la cual es la que busca y trae
    // mediante el metodo addUserToGroup en el caso de este caso y trae la respuesta
    // de si el usuario se agrego o no a la lista de miembros de un grupo
    //El GroupMmbershipDao es injectado por Dagger como parametro en el constructor
    //Constructor must instantiate the cache and assign it the reference to the delegateDao
    @Inject
    public GroupMembershipCachingDao(final GroupMembershipDao delegateDao) {
    // El constructor tiene que assignr el cache a la referencia del delegateDao
        thid.theCache = CacheBuilder.newBuilder()
                .maximumSize(20000)//maxima cantidad de entradas en el cache
                .expireAfterWrite(3, TimeUnit.HOURS) //Tiempo de expiracion del cache en horas
                .build(CacheLoader.from(delegateDao::isUserInGroup));// Se llama al delegateDao para ver si el usuario esta en el grupo


    //vamos a usar el newBuilder para crear el cahe
            //Cada llamada al metodo getUsersInGroup en el cacheManager se va a buscar en el cache
            //Si no lo encuentra, se va a llamar al delegateDao para buscar la informacion
            // y luego se va a guardar en el cache para que lo pueda usar en el futuro
            // Si lo encuentra, se va a devolver de la cache, es una operacion rapida
           //.build(new CacheLoader<GroupMembershipCacheKey, Boolean>() {
        //    @Override
        //    public Boolean load(GroupMembershipCacheKey key) throws Exception {
        //        return delegateDao.addUserToGroup(key.getUserId(), key.getGroupId());
        //    }
        //});

    }

    //Este metodo se va a llamar cuando se va a verificar si un usuario esta en un grupo
    //
    public boolean isUserInGroup(String userId, String groupId) {
        //Obtiene la referencia al cache para usarlo
        // Si no lo encuentra, crea una nueva entrada en el cache con la clave y el valor que devuelve el delegateDao
// No sabemos si va ser un hit or un miss

        return theCache.getUnchecked(new GroupMembershipCacheKey(userId, groupId));
        //secrea un cahe key si el usuario esta en el grupo, devuelve true, sino devuelve false

        //Usamos el getUnchecked para que nos aseguremos que la
        // llamada al delegateDao se haga en este punto del codigo,

    }

}
