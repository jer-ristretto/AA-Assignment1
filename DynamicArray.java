public class DynamicArray<E> {

    private transient E[] array;
    private int size;
    private static final int initialSize = 10;


    public DynamicArray() {
        array = (E[]) new Object[initialSize];
        size = 0;
    }


    public E get(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Invalid index");

        return array[index];
    }


    public int getSize() {
        return size;
    }


    public void set(int index, E e) throws IndexOutOfBoundsException {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Invalid index");

        array[index] = e;
    }


    public void add(E e) {
        if (size < array.length) {
            array[size] = e;
        }
        else {
            E[] newArray = (E[]) new Object[array.length * 2];

            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }

            newArray[array.length] = e;
            array = newArray;
        }

        size++;
    }


    public void remove(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        for (int j = index; j < size - 1; j++)
            array[j] = array[j + 1];

        array[size - 1] = null;

        size--;
    }


    /**
     * Create an array with the size equal to the number of elements in the DynamicArray
     * and pass it to this function.
     */
    public <E> E[] toArray(E[] arr) {
        for (int i = 0; i < size; i++)
            arr[i] = (E) array[i];
        return arr;
    }
}
