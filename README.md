# Appachhi Android SDK

## Getting started

As of now we dont have any distribution option but you can always build by 
running `./gradlew :apppachhisdk:assembleRelease`.You can find the `aar` file
on `appachhisdk` modules's `build` directory

## Feature
- Screen Transition
    - Automatic(Activity only)
    - Manual
- ~~Memory Usage~~
- ~~CPU Usage~~
- ~~Network Usage~~
- ~~API call details~~
## Usage

### Screen Transition

#### Automatic
It is as simple as annotating the activity for which screen transition time 
needs to be computed
```
@AutoActivityTrace
class MyActivity extends AppCompatActivity{
    // Your code
}
```

#### Manual(Recommended)

As of now this is the recommended approach because of limitation of automatic 
method. We will be improving it over the period of time.
```
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