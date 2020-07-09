#include <iostream>
#include <libhalodb.h>

int main(int argc, char* argv[]) {
  graal_isolate_t *isolate = NULL;
  graal_isolatethread_t *thread = NULL;

  if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
    fprintf(stderr, "initialization error\n");
    return 1;
  }

  char *result1 = halodb_open((long)thread);
  std::cout << result1 << std::endl;

  char *result2 = halodb_put((long)thread, "key1", "value1");
  std::cout << result2 << std::endl;

  char *result3 = halodb_put((long)thread, "key2", "value2");
  std::cout << result3 << std::endl;

  char *result4 = halodb_get((long)thread, "key2");
  std::cout << result4 << std::endl;

  return 0;
}
