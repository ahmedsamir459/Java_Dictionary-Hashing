public interface HashTable<T> {
    public void insert(T key);
    public void delete(T key);
    public boolean search(T key);
    public void batchInsert(T[] keys);
    public void batchDelete(T[] keys);
}
