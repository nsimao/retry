# Retry

Easy to use custom retry method implementation.

### Use cases

- You don't have this handled by infrastructure.
- You can't use a library such as `Resilience4J` (https://github.com/resilience4j/resilience4j)
  or `Failsafe` (https://failsafe.dev/).

### Example

```java
MyResponse myResponse= new RetryUtils<MyResponse>().retry(()->myService.getById(id),
        "myCustomLogInfo",
        retryCount,
        Arrays.asList(StatusRuntimeException.class,Errors.NativeIoException.class),
        MyCustomException.class);
```
