package Server.DataBase;

import Anexe.Entry;
import java.io.Serializable;

import Anexe.MaliciousCommandException;
import jakarta.persistence.*;
import java.util.*;

@SuppressWarnings({"SqlSourceToSinkFlow", "unused", "rawtypes", "unchecked", "CallToPrintStackTrace", "TryFinallyCanBeTryWithResources"})
public class Database {
    static Hibernate hu = new Hibernate();

    public static void addEntry(Entry entry){
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.persist(entry);
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static void removeEntry(Entry entry){
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.remove(em.merge(entry));
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static <T extends Entry> T fetchEntry(Class<T> entryClass, Object key){
        T e;
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            e = em.find(entryClass, key);
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
        return e;
    }

    public static void updateEntry(Entry entry){
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            em.merge(entry);
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static <T extends Entry> T reattachEntry(T detachedEntry) {
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        T managedEntry;
        try {
            transaction.begin();
            managedEntry = em.merge(detachedEntry);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
        return managedEntry;
    }

    public static <T extends Entry> List<T> reattachQuery(List<T> detachedEntries) {
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        List<T> managedEntries = new ArrayList<>();
        try {
            transaction.begin();
            for(T entry : detachedEntries) {
                managedEntries.add(em.merge(entry));
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
        return managedEntries;
    }

    public static <T extends Entry> List<T> selectQuery(String query, Class<T> entryClass, Map<String, Object> params){
        List<T> result;
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            TypedQuery<T> qry = em.createQuery(query, entryClass);
            if(params != null){
                for(String key : params.keySet()){
                    qry.setParameter(key, params.get(key));
                }
            }
            result = qry.getResultList();
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
        return result;
    }

    public static void updateQuery(String query, Map<String, Object> params){
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Query qry = em.createQuery(query);
            if(params != null){
                for(String key : params.keySet()){
                    qry.setParameter(key, params.get(key));
                }
            }
            qry.executeUpdate();
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static void nativeQuery(String query, Map<String, Object> params){
        EntityManager em = hu.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Query qry = em.createNativeQuery(query);
            if(params != null){
                for(String key : params.keySet()){
                    qry.setParameter(key, params.get(key));
                }
            }
            qry.executeUpdate();
            transaction.commit();
        } catch(Exception ex){
            if(transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static Serializable commandInterpreter(Serializable command) {
        try {
            Serializable[] cmd = (Serializable[]) command;
            MaliciousCommandException.inspectCommand(cmd);
            Class entityClass;
            switch ((String) cmd[0]) {
                case "add":
                    addEntry((Entry) cmd[1]);
                    return "success";
                case "remove":
                    removeEntry((Entry) cmd[1]);
                    return "success";
                case "fetch":
                    entityClass = Class.forName((String) cmd[1]);
                    return fetchEntry(entityClass, cmd[2]);
                case "merge":
                    updateEntry((Entry) cmd[1]);
                    return "success";
                case "select":
                    entityClass = Class.forName((String) cmd[2]);
                    return (Serializable) selectQuery((String) cmd[1], entityClass, (Map<String, Object>) cmd[3]);
                case "update":
                    updateQuery((String) cmd[1], (Map<String, Object>) cmd[2]);
                    return "success";
                case "reattach":
                    return reattachEntry((Entry) cmd[1]);
                case "reattachList":
                    return (Serializable) reattachQuery((List<Entry>) cmd[1]);
                default:
                    System.out.println("Unknown command: " + cmd[0]);
            }
        } catch(MaliciousCommandException ex) {
            System.out.println("MaliciousCommandException[]: " + ex.getMessage());
        } catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Failed operation: " + command);
        return "fail";
    }
}