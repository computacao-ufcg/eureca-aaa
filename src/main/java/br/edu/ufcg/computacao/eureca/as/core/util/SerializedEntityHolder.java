package br.edu.ufcg.computacao.eureca.as.core.util;

import com.google.gson.Gson;

public class SerializedEntityHolder<T> {

    private String className;
    private String payload;

    public SerializedEntityHolder(T instanceToSerialize) {
        this.className = instanceToSerialize.getClass().getName();
        this.payload = (new Gson()).toJson(instanceToSerialize);
    }

    public T getSerializedEntity() throws ClassNotFoundException {
        return (T) (new Gson()).fromJson(this.payload, Class.forName(this.className));
    }

    @Override
    public String toString() {
        return (new Gson()).toJson(this, SerializedEntityHolder.class);
    }
}
