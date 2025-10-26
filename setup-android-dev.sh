#!/bin/bash
# Android development environment setup for Ubuntu VM

set -e

echo "=== Updating system ==="
sudo apt update && sudo apt upgrade -y

echo "=== Installing essential packages ==="
sudo apt install -y wget unzip git curl build-essential zip unzip zlib1g-dev

echo "=== Installing OpenJDK 17 ==="
if ! dpkg -l | grep -q openjdk-17-jdk; then
    sudo apt install -y openjdk-17-jdk
else
    echo "OpenJDK 17 already installed"
fi

echo "=== Setting JAVA_HOME and PATH ==="
if ! grep -q "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" ~/.bashrc; then
    echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
    echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
fi
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "=== Verifying Java ==="
java -version

echo "=== Installing Android SDK command-line tools ==="
ANDROID_SDK_ROOT=$HOME/Android/Sdk
if [ ! -d "$ANDROID_SDK_ROOT/cmdline-tools/latest" ]; then
    mkdir -p $ANDROID_SDK_ROOT/cmdline-tools
    cd /tmp
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdline-tools.zip
    unzip cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest
    rm cmdline-tools.zip
else
    echo "Android SDK command-line tools already installed"
fi

echo "=== Setting ANDROID_HOME and updating PATH ==="
if ! grep -q "ANDROID_HOME=$ANDROID_SDK_ROOT" ~/.bashrc; then
    echo "export ANDROID_HOME=$ANDROID_SDK_ROOT" >> ~/.bashrc
    echo "export PATH=\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools:\$PATH" >> ~/.bashrc
fi
export ANDROID_HOME=$ANDROID_SDK_ROOT
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH

echo "=== Installing Android SDK platform tools and build tools ==="
yes | sdkmanager --licenses || true
if [ ! -d "$ANDROID_SDK_ROOT/platform-tools" ]; then
    sdkmanager --install "platform-tools" "platforms;android-34" "build-tools;34.0.0"
else
    echo "Platform tools and build tools already installed"
fi

echo "=== Installing NDK and CMake ==="
if [ ! -d "$ANDROID_SDK_ROOT/ndk/23.1.7779620" ]; then
    sdkmanager --install "ndk;23.1.7779620" "cmake;3.22.1"
else
    echo "NDK and CMake already installed"
fi

echo "=== Setting ANDROID_NDK_HOME ==="
if ! grep -q "ANDROID_NDK_HOME=\$ANDROID_HOME/ndk/23.1.7779620" ~/.bashrc; then
    echo "export ANDROID_NDK_HOME=\$ANDROID_HOME/ndk/23.1.7779620" >> ~/.bashrc
fi
export ANDROID_NDK_HOME=$ANDROID_HOME/ndk/23.1.7779620

echo "=== Installing Go 1.23.3 ==="
if [ ! -d "/usr/local/go" ] || ! /usr/local/go/bin/go version | grep -q "go1.23.3"; then
    cd /tmp
    wget https://go.dev/dl/go1.23.3.linux-amd64.tar.gz
    sudo rm -rf /usr/local/go
    sudo tar -C /usr/local -xzf go1.23.3.linux-amd64.tar.gz
    rm go1.23.3.linux-amd64.tar.gz
else
    echo "Go 1.23.3 already installed"
fi

echo "=== Setting Go environment variables ==="
if ! grep -q "export PATH=/usr/local/go/bin:\$PATH" ~/.bashrc; then
    echo 'export PATH=/usr/local/go/bin:$PATH' >> ~/.bashrc
fi
if ! grep -q "export GOPATH=\$HOME/go" ~/.bashrc; then
    echo 'export GOPATH=$HOME/go' >> ~/.bashrc
    echo 'export PATH=$GOPATH/bin:$PATH' >> ~/.bashrc
fi
export PATH=/usr/local/go/bin:$PATH
export GOPATH=$HOME/go
export PATH=$GOPATH/bin:$PATH

echo "=== Verifying Go installation ==="
go version

echo "=== Installing gomobile ==="
if [ ! -f "$HOME/go/bin/gomobile" ]; then
    go install golang.org/x/mobile/cmd/gomobile@v0.0.0-20230531173138-3c911d8e3eda
else
    echo "gomobile already installed"
fi

echo "=== Initializing gomobile with NDK ==="
if [ ! -d "$HOME/.gomobile" ]; then
    ANDROID_NDK_HOME=$ANDROID_HOME/ndk/23.1.7779620 gomobile init -ndk $ANDROID_HOME/ndk/23.1.7779620
else
    echo "gomobile already initialized"
fi

echo "=== Verifying SDK installation ==="
sdkmanager --list

echo "=== All done! ==="
echo "Please restart your terminal or run 'source ~/.bashrc' to apply environment variables."
echo ""
echo "Summary of installed tools:"
echo "  - OpenJDK 17"
echo "  - Android SDK Command-line Tools"
echo "  - Android NDK 23.1.7779620"
echo "  - CMake 3.22.1"
echo "  - Go 1.23.3"
echo "  - gomobile"
