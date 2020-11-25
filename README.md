# View2Pdf-Android
Android Library to covert view to pdf.

## Setup
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.ASahu16:View2Pdf-Android:0.1.1'
}
```


## Usage

```Java
		View2Pdf view2Pdf = new View2Pdf(MainActivity.this);
        view2Pdf.create_pdf(nestedScrollView);
```

