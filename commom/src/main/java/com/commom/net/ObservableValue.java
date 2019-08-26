package com.commom.net;

import java.io.Serializable;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/8/30 15:23
 * <p/>
 * Description:
 */
@SuppressWarnings("unused")
public class ObservableValue<T> implements Serializable {
    private T mValue;

    /**
     * Wraps the given object and creates an observable object
     *
     * @param value
     *         The value to be wrapped as an observable.
     */
    public ObservableValue(T value) {
        mValue = value;
    }

    /**
     * Creates an empty observable object
     */
    public ObservableValue() {
    }

    /**
     * @return the stored value.
     */
    public T get() {
        return mValue;
    }

    /**
     * Set the stored value.
     */
    public void set(T value) {
        if (value != mValue) {
            mValue = value;
        }
    }
}
