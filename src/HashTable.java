public interface HashTable<T> {
    boolean insert(T key);
    boolean delete(T key);
    boolean search(T key);
    int batchInsert(T[] keys);
    void batchDelete(T[] keys);
    int getCollisionCount();
    int getRebuildCount();
    void print();
}
