package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;

import java.lang.reflect.Constructor;
import java.util.Arrays;

// Each microservice has to have its own ClassFactory

public class ClassFactory {

    public Object createPluginInstance(String pluginClassName, String ... params)
            throws FatalErrorException {

        Object pluginInstance;
        Constructor<?> constructor;

        try {
            Class<?> classpath = Class.forName(pluginClassName);
            if (params.length > 0) {
                Class<String>[] constructorArgTypes = new Class[params.length];
                Arrays.fill(constructorArgTypes, String.class);
                constructor = classpath.getConstructor(constructorArgTypes);
            } else {
                constructor = classpath.getConstructor();
            }

            pluginInstance = constructor.newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new FatalErrorException(String.format(Messages.UNABLE_TO_FIND_CLASS_S, pluginClassName));
        } catch (Exception e) {
            throw new FatalErrorException(e.getMessage(), e);
        }
        return pluginInstance;
    }
}
