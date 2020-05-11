#include <jni.h>
#include <string>
#include <sys/system_properties.h>


extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getmanufacturer(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Manufacturer,Model,SDK */

        char manufacturer[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenmanufacturer = __system_property_get("ro.product.manufacturer",manufacturer);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenmanufacturer <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(manufacturer,"NA");
    }
    return env->NewStringUTF(manufacturer);
}
extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getmodel(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char model[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenmodel = __system_property_get("ro.product.model",model);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenmodel <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(model,"NA");
    }
    return env->NewStringUTF(model);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getsdk(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char SDK[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenSDK = __system_property_get("ro.build.version.sdk",SDK);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenSDK <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(SDK,"NA");
    }
    return env->NewStringUTF(SDK);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getrelease(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char buildrelease[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenrelease = __system_property_get("ro.build.version.release",buildrelease);

    /* If calls to get system property returns Zero, fill the field with NA instead
      * of garbage value */
    if( lenrelease <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(buildrelease,"NA");
    }

    return env->NewStringUTF(buildrelease);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getbrand(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char brand[PROP_VALUE_MAX + 1];

   /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenbrand = __system_property_get("ro.product.brand",brand);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenbrand <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(brand,"NA");
    }

    /* If calls to get system property returns desired values, format the string
    * before displaying it */
    return env->NewStringUTF(brand);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getlcddensity(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char LCD[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenlcd = __system_property_get("ro.sf.lcd_density",LCD);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenlcd <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(LCD,"NA");
    }
    /* If calls to get system property returns desired values, format the string
    * before displaying it */
    return env->NewStringUTF(LCD);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_example_myapplicationforc_MainActivity_getCPUdetails(JNIEnv* env, jobject /*this*/)
{
    /* Strings to store device details Model */
    char CPU[PROP_VALUE_MAX + 1];

    /* this method retrieves and stores the device details to the character
    * array and returns the length. Need to confirm the deprecation of the system
    * call __system_property_get */
    int lenlcd = __system_property_get("ro.product.cpu.abi",CPU);

    /* If calls to get system property returns Zero, fill the field with NA instead
     * of garbage value */
    if( lenlcd <= 0)
    {
        printf(" Device details returned NULL \n");
        strcpy(CPU,"NA");
    }
    /* If calls to get system property returns desired values, format the string
    * before displaying it */
    return env->NewStringUTF(CPU);
}
