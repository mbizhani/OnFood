import org.devocative.artemis.Context

import static org.junit.jupiter.api.Assertions.*

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
	assertNotNull(rsBody.userId)
	assertNotNull(rsBody.token)
}

def assertRs_profile(Context ctx, Map rsBody) {
	assertEquals(ctx.vars.firstName, rsBody.firstName)
	assertEquals(ctx.vars.lastName, rsBody.lastName)
	assertEquals(ctx.vars.cell, rsBody.cell)
	assertEquals("anonymous", rsBody.createdBy)
	assertNotNull(rsBody.createdDate)
	assertNull(rsBody.lastModifiedBy)
	assertNull(rsBody.lastModifiedDate)
	assertEquals(0, rsBody.version)
}