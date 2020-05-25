package sqlruparse;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class PsqlStoreTest {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    @Mock
    private Statement defaultStatement;
    @Mock
    private ResultSet resultSet;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTest() throws SQLException {
        when(connection.prepareStatement(any())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        PsqlStore store = new PsqlStore(connection);
        boolean out = store.save(new Post("sgsdfg", "name", "sadfgsd", LocalDateTime.now()));
        assertThat(out, is(true));
    }

    @Test
    public void getAllTest() throws SQLException {
        List<Post> expected = List.of(new Post("sgsdfg", "sgsdfg", "sgsdfg", LocalDateTime.now()),
                new Post("sgsdfg", "sgsdfg", "sgsdfg", LocalDateTime.now()));
        when(connection.createStatement()).thenReturn(defaultStatement);
        when(defaultStatement.executeQuery(any())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(any())).thenReturn("sgsdfg");
        when(resultSet.getTimestamp(any())).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        PsqlStore store = new PsqlStore(connection);
        List<Post> out = store.getAll();
        assertThat(out.toString(), is(expected.toString()));
    }

    @Test
    public void findByIdTest() throws SQLException {
        Post expected = new Post("sgsdfg", "sgsdfg", "sgsdfg", LocalDateTime.now());
        when(connection.prepareStatement(any())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(any())).thenReturn("sgsdfg");
        when(resultSet.getTimestamp(any())).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        PsqlStore store = new PsqlStore(connection);
        Post out = store.findById("1");
        assertThat(out.toString(), is(expected.toString()));
    }

    @Test
    public void whenSaveCatchSQLExceptionTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);
        PsqlStore store = new PsqlStore(connection);
        boolean out = store.save(new Post("sgsdfg", "name", "sadfgsd", LocalDateTime.now()));
        assertThat(out, is(false));
    }

    @Test
    public void whengetAllCatchSQLExceptionTest() throws SQLException {
        when(connection.createStatement()).thenThrow(SQLException.class);
        PsqlStore store = new PsqlStore(connection);
        List<Post> out = store.getAll();
        assertThat(out.size(), is(0));
    }

    @Test
    public void whenfindByIdCatchSQLExceptionTest() throws SQLException {
        doThrow(SQLException.class).when(connection).prepareStatement(anyString());
        PsqlStore store = new PsqlStore(connection);
        Post out = store.findById("1");
        assertNull(out);
    }
}