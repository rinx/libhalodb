#include <iostream>
#include <libhalodb.h>

int main(int argc, char* argv[]) {
  graal_isolate_t *isolate = NULL;
  graal_isolatethread_t *thread = NULL;

  if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
    fprintf(stderr, "initialization error\n");
    return 1;
  }

  int result1 = halodb_open(thread, ".halodb");
  std::cout << result1 << std::endl;

  int result2 = halodb_put(thread, "key1", "value1");
  std::cout << result2 << std::endl;

  int result3 = halodb_put(thread, "key2", "value2");
  std::cout << result3 << std::endl;

  long result4 = halodb_size(thread);
  std::cout << result4 << std::endl;

  char *result5 = halodb_get(thread, "key2");
  std::cout << result5 << std::endl;

  int result6 = halodb_delete(thread, "key2");
  std::cout << result6 << std::endl;

  long result7 = halodb_size(thread);
  std::cout << result7 << std::endl;

  return 0;
}
