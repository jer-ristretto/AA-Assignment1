import java.io.PrintWriter;

public class DynamicArray<E> {

    private Object[] array;
    private int size;
    private static final int initialSize = 2;


    public DynamicArray() {
        array = new Object[initialSize];
        size = 0;
    }


    public E get(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Invalid index");

        return (E) array[index];
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
            Object[] newArray = new Object[array.length * 2];

            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }

            newArray[array.length] = e;
            array = newArray;
        }

        size++;
    }


    public void insert(int index, E e) throws IndexOutOfBoundsException {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        if (size < array.length) {
            for (int j = size; j > index; j--) {
                array[j] = array[j - 1];
            }

            array[index] = e;
        }
        else {
            Object[] newArray = new Object[array.length * 2];

            for (int i = 0; i < index; i++) {
                newArray[i] = array[i];
            }

            for (int j = array.length; j > index; j--) {
                newArray[j] = array[j - 1];
            }

            newArray[index] = e;
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
}
