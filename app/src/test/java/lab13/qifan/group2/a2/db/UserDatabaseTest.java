    package lab13.qifan.group2.a2.db;

    import lab13.qifan.group2.a2.models.User;
    import lab13.qifan.group2.a2.models.UserType;
    import lab13.qifan.group2.a2.utilities.PasswordEncryptor;
    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    import static org.junit.jupiter.api.Assertions.*;

    public class UserDatabaseTest {

        private UserDatabase userDatabase;
        private User testUser;
        private PasswordEncryptor passwordEncryptor;

        @BeforeEach
        public void setUp() {
            userDatabase = new UserDatabase();
            testUser =  new User(null, null, null, 0, null, null, null);
            testUser.setUsername("testUser");
            String hashedPassword = passwordEncryptor.hashPassword("testPassword");
            String salt = hashedPassword.substring(0, 32);
            testUser.setPassword(hashedPassword);
            testUser.setEmail("test@email.com");
            testUser.setPhone(123456789);
            testUser.setIdKey("testKey");
            testUser.setUserType(UserType.REGULAR);
            testUser.setSalt(salt);

            userDatabase.addUser(testUser);
        }
        @AfterEach
        public void cleanup()
        {
            try (Connection connection = DriverManager.getConnection(userDatabase.getDBURL());
                 PreparedStatement stmt = connection.prepareStatement(
                         "DELETE FROM users WHERE username = ? AND idKey = ?")) {
                stmt.setString(1, testUser.getUsername());
                stmt.setString(2, testUser.getIdKey());
                stmt.execute();
            } catch (NullPointerException | SQLException e) {
                e.printStackTrace();
            }
        }
         @Test
         public void testAddUser() {
             User newUser = new User(null, null, null, 0, null, null, null);
             newUser.setUsername("newUser");
             newUser.setPassword("newPassword");
             newUser.setEmail("new@email.com");
             newUser.setPhone(987654321);
             newUser.setIdKey("newKey");
             newUser.setUserType(UserType.REGULAR);
             newUser.setSalt("newSalt");

             boolean result = userDatabase.addUser(newUser);
             assertTrue(result);
             try (Connection connection = DriverManager.getConnection(userDatabase.getDBURL());
                  PreparedStatement stmt = connection.prepareStatement(
                          "DELETE FROM users WHERE username = ? AND idKey = ?")) {
                 stmt.setString(1, newUser.getUsername());
                 stmt.setString(2, newUser.getIdKey());
                 stmt.execute();
             } catch (NullPointerException | SQLException e) {
                 e.printStackTrace();
             }
         }
        @Test
        public void testGetUser() {
            User user = userDatabase.getUser("testUser");
            assertNotNull(user);
            assertEquals("testUser", user.getUsername());
        }

        @Test
        public void testAuthenticateUser() {
            boolean isAuthenticated = userDatabase.authenticateUser("testUser", "testPassword");
            assertTrue(isAuthenticated);
        }

        // @Test
        // public void testCheckIdKeyExists() {
        //     boolean exists = userDatabase.checkIdKeyExists("testKey");
        //     assertTrue(exists);
        // }

        // @Test
        // public void testUpdateUser() {
        //     testUser.setEmail("updated@email.com");
        //     boolean updated = userDatabase.updateUser(testUser);
        //     assertTrue(updated);

        //     User user = userDatabase.getUser("testUser");
        //     assertEquals("updated@email.com", user.getEmail());
        // }
    }
