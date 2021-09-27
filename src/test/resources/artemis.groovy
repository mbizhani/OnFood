import org.devocative.artemis.Context
import org.junit.jupiter.api.Assertions

def before(Context ctx) {
    def password = generate(5, 'a'..'z')
    def encPass = Base64.getEncoder().withoutPadding().encodeToString(password.getBytes())
    ctx.addVar("password", encPass)
    Artemis.log("Password: main=${password} enc=${encPass}")
}

def generate(int n, List<String>... alphaSet) {
    def list = alphaSet.flatten()
    new Random().with {
        (1..n).collect { list[nextInt(list.size())] }.join()
    }
}

// ------------------------------

def assertRs_register(Context ctx, Map rsBody) {
    Assertions.assertNotNull(ctx.vars.cell)
    Assertions.assertNotNull(ctx.vars.firstName)
    Assertions.assertNotNull(ctx.vars.lastName)

    Assertions.assertNotNull(rsBody.userId)
    Assertions.assertNotNull(rsBody.token)
}