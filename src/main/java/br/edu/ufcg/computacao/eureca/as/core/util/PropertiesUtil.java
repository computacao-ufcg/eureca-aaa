package br.edu.ufcg.computacao.eureca.as.core.util;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.FatalErrorException;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class.getName());

    public static Properties readProperties(String fileName) throws FatalErrorException {
        Properties prop = new Properties();
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(fileName);
            prop.load(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new FatalErrorException(String.format(Messages.PROPERTY_FILE_S_NOT_FOUND, fileName), e);
        } catch (IOException e) {
            throw new FatalErrorException(e.getMessage(), e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error(String.format(Messages.UNABLE_TO_CLOSE_FILE_S, fileName), e);
                }
            }
        }
        return prop;
    }
}

