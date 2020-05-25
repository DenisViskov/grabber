package sqlruparse;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class PsqlStoreTest {

    @Mock
    Connection connection;
    @Mock
    PreparedStatement statement;
    @Mock
    Statement defaultStatement;

    @Test
    public void saveTest() throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
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
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(any())).thenReturn(resultSet);
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
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
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
        Connection connection = mock(Connection.class);
        doThrow(SQLException.class).when(connection).prepareStatement(any());
        PsqlStore store = new PsqlStore(connection);
        boolean out = store.save(new Post("sgsdfg", "name", "sadfgsd", LocalDateTime.now()));
        assertThat(out, is(false));
    }

    @Test
    public void whengetAllCatchSQLExceptionTest() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenThrow(SQLException.class);
        PsqlStore store = new PsqlStore(connection);
        List<Post> out = store.getAll();
        assertThat(out.size(), is(0));
    }

    @Test
    public void whenfindByIdCatchSQLExceptionTest() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(any())).thenThrow(SQLException.class);
        PsqlStore store = new PsqlStore(connection);
        Post out = store.findById("");
        assertThat(out, is(isNull()));
    }

    @After
    public void downMock() {
        Mockito.reset();
    }
}