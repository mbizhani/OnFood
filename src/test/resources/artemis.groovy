import org.devocative.artemis.Context

def init(Context ctx) {
}

def generate(int n, List<String>... alphaSet) {
    def list = alphaSet.flatten()
    new Random().with {
        (1..n).collect { list[nextInt(list.size())] }.join()
    }
}