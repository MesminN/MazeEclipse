package fr.tp.maze.serialization;

import fr.tp.maze.exceptions.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMazePersistenceManagerTest {
	  @Rule
	  public final ExpectedException exception = ExpectedException.none();
    FileMazePersistenceManager fileMazePersistenceManager;
    FileMazePersistenceManager objectMazePersistenceManager;

    @Before
    public void setUp() {
        fileMazePersistenceManager = new FileMazePersistenceManager();
        objectMazePersistenceManager = new SerializationFileMazePersistenceManager();
    }

    @Test(expected = EmptyFileMazePersistenceException.class)
    public void doRead_EmptyFile_throwEmptyFileMazePersistenceException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheFichierVide.maze");
        fileMazePersistenceManager.doRead(path);
    }

    @Test(expected = ErroneousArrivalNumberMazeException.class)
    public void doRead_multipleArrivals_throwErroneousArrivalNumberMazeException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheMultiplesArrivees.maze");
        fileMazePersistenceManager.doRead(path);
    }

    @Test(expected = ErroneousDepartureNumberMazeException.class)
    public void doRead_multipleDepartures_throwErroneousDepartureNumberMazeException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheDepartMultiples.maze");
        fileMazePersistenceManager.doRead(path);
    }

    @Test(expected = IncoherentColumnsSizeFileMazePersistenceException.class)
    public void doRead_incoherentColumnsSizes_throwIncoherentColumnsSizeFileMazePersistenceException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheErreurColones.maze");
        fileMazePersistenceManager.doRead(path);
    }

    @Test(expected = UnknownCharacterFileMazePersistenceException.class)
    public void doRead_wrongSymbols_throwUnknownCharacterFileMazePersistenceException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheErreurSymboles.maze");
        fileMazePersistenceManager.doRead(path);
    }

    @Test(expected = UnknownDataTypeSerializationFileMazePersistenceException.class)
    public void doRead_wrongDataType_throwUnknownDataTypeSerializationFileMazePersistenceException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheErreurCast.maze");
        objectMazePersistenceManager.doRead(path);
    }

    @Test(expected = UnknownFileExtensionFileMazePersistenceException.class)
    public void doRead_wrongFileExtension_throwUnknownFileExtensionFileMazePersistenceException() throws IOException {
        String path = getFilePathFromResourcesTestData("labyrintheWrongFileExtension.txt");
        objectMazePersistenceManager.doRead(path);
    }

    private String getFilePathFromResourcesTestData(String name) {
        Path resourceDirectory = Paths.get("src","test","resources", "data", name);
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        return absolutePath;
    }
}