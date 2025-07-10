package printer;

import java.util.logging.*;

public class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    static {
        // Disattiva i logger parent (evita duplicazione messaggi)
        LOGGER.setUseParentHandlers(false);

        // Crea un handler per la console
        ConsoleHandler handler = new ConsoleHandler();

        // Imposta il livello desiderato (INFO, WARNING, etc.)
        handler.setLevel(Level.ALL);
        LOGGER.setLevel(Level.ALL);

        // Formatter minimale: solo il messaggio
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord lRecord) {
                return lRecord.getMessage() + System.lineSeparator();
            }
        });

        // Rimuovi eventuali altri handler e aggiungi il nostro
        LOGGER.addHandler(handler);
    }

    public static void print(String msg) {
        LOGGER.info(msg);
    }
}
