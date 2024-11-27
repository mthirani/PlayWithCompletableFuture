import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestingCompletableFuture {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println("Hello, World!");
    UserInfo userInfo = new UserInfo("Ihor", "ihor@dremio.com", "12345");
    Thread thread1 = new Thread(new UserInfoThread(userInfo));
    Thread thread2 = new Thread(new UserInfoThread(userInfo));
    Thread thread3 = new Thread(new UserInfoThread(userInfo));
    thread1.start();
    thread2.start();
    thread3.start();
    try {
      System.out.println("Thread1: " + thread1.getName());
      thread1.join();
      System.out.println("Thread2: " + thread2.getName());
      thread2.join();
      System.out.println("Thread3: " + thread3.getName());
      thread3.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    ExecutorService executor = Executors.newFixedThreadPool(3);

    CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
      // Some long-running operation
      return userInfo.getValue();
    }, executor);
    CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
      // Some long-running operation
      return userInfo.getValue();
    }, executor);

    CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
    allFutures.join();
    int result1 = future1.get();
    int result2 = future2.get();
    System.out.println("value from c.future1: " + result1);
    System.out.println("value from c.future2: " + result2);

  }
}
class UserInfoThread implements Runnable {

  UserInfo userInfo;
  UserInfoThread(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  @Override
  public void run() {
    userInfo.getValue();
  }
}
class UserInfo {
  private String name;
  private String email;
  private String password;

  public UserInfo(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public int getValue() {
    UserInfo userInfo = new UserInfo("John", "john@dremio.com", "password");
    System.out.println("UserInfo: " + userInfo);
    System.out.println("UserInfo#Hash: " + userInfo.hashCode());
    int value = 1;
    for (int i = 0; i < 1000; i++) {
      value += 1;
    }

    System.out.println("value: " + value);

    return value;
  }
}
