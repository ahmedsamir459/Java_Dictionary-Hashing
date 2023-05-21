public interface HashTable<T> {
    public boolean insert(T key);
    public boolean delete(T key);
    public boolean search(T key);
    public void batchInsert(T[] keys);
    public void batchDelete(T[] keys);
}
