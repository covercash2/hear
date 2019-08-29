use std::ffi::{CStr, CString};
use std::os::raw::c_char;

#[no_mangle]
pub extern "C" fn rust_greeting(to: *const c_char) -> *mut c_char {
    let c_str = unsafe { CStr::from_ptr(to) };
    let recipient = match c_str.to_str() {
        Err(_) => "there",
        Ok(string) => string,
    };

    CString::new("Hello ".to_owned() + recipient)
        .unwrap()
        .into_raw()
}

#[cfg(target_os = "android")]
#[allow(non_snake_case)]
pub mod android {
    use super::*;
    use jni::{
        objects::{JClass, JString},
        sys::jstring,
        JNIEnv,
    };

    #[no_mangle]
    pub unsafe extern "C" fn Java_dev_covercash_hear_jni_Greeting_greeting(
        env: JNIEnv,
        _: JClass,
        java_pattern: JString,
    ) -> jstring {
        let world = rust_greeting(
            env.get_string(java_pattern)
                .expect("invalid pattern string")
                .as_ptr(),
        );
        let world_ptr = CString::from_raw(world);
        let output = env
            .new_string(world_ptr.to_str().expect("unable to deref world pointer"))
            .expect("couldn't create java string");

        return output.into_inner();
    }
}

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }
}
