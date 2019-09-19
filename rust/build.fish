# requires cargo-ndk https://github.com/bbqsrc/cargo-ndk
function build --description "build library for android"
    cargo ndk --target aarch64-linux-android --android-platform 26 -- build --release
end
