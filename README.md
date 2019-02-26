
# Appachhi Android SDK
A simple and easy to use performance monitoring and instrumentation library for
Android

SDK provides functionality to instrument the screen transition on Android i.e.
instrument the time take to render the entire view hierarchy for a given screen.
In order to provide ease of use for the developer,this can be achieved
automatically for an activity(Plugin needed).This can also be done manually
where developer has full control of deciding when a screen start and when the
screen ends.Information are logged onto the logcat at `Log.INFO` level

It provides alot of performance metric for the app when it is being used.It
provides metric for Memory Usage, CPU Usage and Network Usage along with GC
execution data.All these information are polled on a regular interval by the SDK
and are logged onto the logcat at `Log.INFO` level

In order to properly instrument the method execution and trace the method,SDK
along with Plugin provides method trace functionallity ensuring minimal overhead
for the app during the runtime.In order to relavant information for the app,
Method Tracing method will only trace method which are part of your project i.e
methods from other library are not traced.Method Trace Information are flushed 
on to the logcat at level `Log.INFO`
## Performane Metric

SDK support following performane monitoring:
- CPU Usage
- Device Level CPU Usage(Min - 0% and Max - 100%)
- App Level CPU Usage(Min - 0% and Max - 100%)

- Memory Usage
- Threshold Memory
- Total PSS Memory
- Total Private Dirty Memory
- Total Share Dirty Memory
- Java Heap Memory
- Native Heap
- Code Memory (Android M and above)
- Stack Memory (Android M and above)
- Graphic Memory (Android M and above)
- Other Memory (Android M and above)
- System Resource Memory (Android M and above)
- Total Swap Memory (Android M and above)

For Memory usage detail,some information are available only above Android M and
above. Memory metric are represented in KiloBytes

- Network Usage
- Data Sent
- Data Recived

Network usage metric are in term of KiloBytes

- GC Call
-   Reason for running GC
-   Name of the GC
-   Object Freed
-   Object Freed Size
-   AllocSpace Object Free
-   AllocSpace Object Fred Size
-   Large Object Free Percentage
-   Large Object Total Size
-   Pause Time for GC(Main Thread Pause)
-   GC Running Time

GC call information will only be avilable if the GC execution was forced by the
developer or if the GC runtime is more than 100ms or if the GC pause time is more
than 5 ms

## Download

In order to add the SDK,update your app's `build.gradle` file with the following
code

```gradle
dependencies {
  implementation 'com.appachhi:sdk:0.0.1'
}
```
or 
```xml
<dependency>
  <groupId>com.appacchi</groupId>
  <artifactId>sdk</artifactId>
  <version>0.0.1</version>
</dependency>
```

In order to add the Plugin, udpate your project's `build.gradle` file with the 
following code
```gradle
 classpath 'com.appachhi:plugin:0.0.1'
```
As of now,Appachhi plugin is not available on Maven Build Tool.

Also add the following code at the top of you app's `build.gradle` file
```gradle
apply plugin: 'com.appachhi.plugin'
```

You can build the SDK from source by running the gradle task as follows
```gradle
./gradlew :apppachhisdk:assembleRelease
```
After running the task, you can find the AAR inthe build directory of
`apppachhisdk` module.

Similarly in order to build the Plugin,you can run the gradle task as follows
```gradle
./gradlew :appachhiplugin:jar
```
You can find the Jar for the Plugin the in build directory of `appachhiplugin` module


## How do I use it?

SDK has been designed considering the ease of use in mind.We want the developers
not to focus on setting up the library instead building an amazing app.So for 
performance moniory like CPU Usage, Memory Usage, Network Usage and GC Runnning 
Info,you dont have to much.Just initualize the SDK by adding the following code
in you `Application` class

```java
Appachhi.init(this)
```

But some task like screen transtion intrumention,we need to make some adjustment
to your code

### Screen Transition

#### Automatic
It is as simple as annotating the activity for which screen transition time
needs to be computed.For this to work, you need to make sure you have added the 
`appachhiplugin`

```java
@AutoActivityTrace
class MyActivity extends AppCompatActivity{
    // Your code
}
```

#### Manual(Recommended)

As of now this is the recommended approach because of limitation of automatic
method. We will be improving it over the period of time.
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default way
        ScreenTransitionManager.getInstance().beginTransition(this);

        // If there is mutliple instance of the activity running at the
        // same time or if you want to give a custom name
        ScreenTransitionManager.getInstance().beginTransition(this,"Your Screen Name");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Default way
        ScreenTransitionManager.getInstance().endTransition(this);

        // Use this if you have  give the custom name as shown above.Also keep
        // in mind to give the same name which was given during transition start
        ScreenTransitionManager.getInstance().endTransition(this,"Your Screen Name");
    }
}

```

### Method Trace

Method Tracing feature is not the part of `appachhisdk`.In order to use the 
feature, you need to import the `appachhiplugin` as explained in the Download 
section of the ReadMe