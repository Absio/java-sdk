# The Basics

## About
The Absio SDK provides basic cryptographic operations: key generation, key exchange, encyption/decryption, HMAC, signing, hashing and elliptic curve cryptography (ECC). A custom Integrated Encryption Scheme (IES) for confidentiality and source verification is included as part of the ECC features.

## Users
In the SDK a user is a person or system. Users are represented as a GUID and only have context when using Absio IES to encrypt and decrypt. Users will have a set of keys. See [Keys](#keys) for more information on the types of keys supported.

## Keys
There are two types of keys related to [Users](#users): Signing and Derivation. Signing keys are used to source verification (who encrypted the data). Derivation keys are used as part of the key generation process (key exchange for encrypt/decrpyt operations).

# Quick Start

## Getting Started
This Quick start guide is intended to help you begin playing around with the Absio SDKs right away.

**Need more info on the technology and tools?** Check out **[The Basics](#the basics)** section.

**Want to dig deeper into the Absio SDKs?**   See our complete **[API documentation](https://absio.github.io/java-sdk/)**.

## Java
This SDK was written with the Java 7 language level, but was tested exclusively against Java 8.

### JCE
Java uses the JCE to perform all cryptographic operations.  The SDK requires one modification to the JDK/JRE being used (for the JCE) as well as one initialization to ensure the JCE Absio depends on is used.

#### Java Cryptography Extension (JCE) Unlimited Strength
In order to use the SDK to perform any cryptography, the JDK/JRE must be updated for the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)(this is for JDK 1.8).

#### Absio's Required JCE
The SDK depends on the OpenSSLProvider from Conscrypt as the JCE.  It is set by simply calling
~~~
Security.insertProviderAt(new OpenSSLProvider(), 1);
~~~
before any SDK operations are performed.

## Install
Through maven you can include the SDK dependency.
~~~
        <dependency>
            <groupId>com.absio</groupId>
            <artifactId>absio-sdk</artifactId>
            <version>SDK_VERSION_NUMBER</version>
        </dependency>
~~~
For 1.3.0 it would be
~~~
        <dependency>
            <groupId>com.absio</groupId>
            <artifactId>absio-sdk</artifactId>
            <version>1.3.0</version>
        </dependency>
~~~

This will only work if you also reference the Absio Nexus.
~~~
        <repository>
            <id>absio-nexus</id>
            <url>https://nexus.absio.com/repository/maven-releases/</url>
        </repository>
~~~

## Initialize
See the [Java](#java) section above to ensure that the JDK/JRE are updated to the unlimited strength and the JCE is initialized by calling
~~~
Security.insertProviderAt(new OpenSSLProvider(), 1);
~~~
before the SDK is used.

## Symmetric Features
The SDK will perform symmetric key generation, encryption and decryption. By default the SDK will use AES 256.

### Symmetric Key Generation
~~~
CipherHelper helper = new CipherHelper();
byte[] aes256Key = helper.generateKey();
~~~

### Symmetric Encryption
~~~
CipherHelper helper = new CipherHelper();
byte[] ciphertextBytes = helper.encrypt(keyBytes, ivBytes, plaintextBytes);
~~~

### Symmetric Decryption
~~~
CipherHelper helper = new CipherHelper();
byte[] plaintextBytes = helper.decrypt(keyBytes, ivBytes, ciphertextBytes);
~~~

## Hashing
By default the SDK will use SHA384 for hashing operations.

~~~
MessageDigestHelper helper = new MessageDigestHelper();
byte[] hashBytes = helper.digest(dataBytes);
~~~

## Absio Integrated Encryption Scheme
Included in the ECC module is a special Integrated Encryption Scheme. In this scheme ECDH is computed using the recipient user's public derivation key. The resultant key is used to encrypt the data. That data is then signed with the sending user's signing key. By default this will use AES 256 CTR NoPadding along with ECDH and ECDSA both using curve P384. There is also a [simple command line utility](ies.md) to perform the encrypt and decrypt operations.

### Absio IES Encrypt
~~~
AbsioIESHelper helper = new AbsioIESHelper();
byte[] iesDataBytes = helper.encrypt(plaintextBytes, signingPrivateKey, derivationPublicKey, senderId, objectId
~~~

### Absio IES Decrypt
~~~
AbsioIESHelper helper = new AbsioIESHelper();
byte[] plaintextBytes = helper.decrypt(iesData, signingPublicKey, derivationPrivateKey);
~~~

## Key Derivation Function
The SDK can be used to derive keys using KDF2 as well. By default it will generate keys using a SHA384 Message Digest.

~~~
KDF2Helper helper = new KDF2Helper();
byte[] keyBytes = helper.deriveKey(secretBytes, keySizeInBytes);
~~~

## Password Based Key Derivation Function
The SDK can be used to derive keys using PBKDF2 as well. By default it will use HMACSHA384, AES256 in CTR mode with no padding and UTF-8 encoding.

### Key Generation

~~~
PBKDF2Helper helper = new PBKDF2Helper();
byte[] keyBytes = helper.generateDerivedKey(password, salt, iterationCount);
~~~

### Encrypt

~~~
PBKDF2Helper helper = new PBKDF2Helper();
byte[] formattedCiphertextBytes = helper.encryptToFormat(plaintextBytes, saltBytes, "password", 100000);
~~~

### Decrypt

~~~
PBKDF2Helper helper = new PBKDF2Helper();
byte[] plaintextBytes = helper.decryptFromFormat(formattedCiphertextBytes, "password", 100000);
~~~

## Asymmetric Features
The SDK can generate key pairs and perform [encryption](#absio-ies-encrypt) and [decryption](#absio-ies-decrypt) with [Absio IES](#absio-integrated-encryption-scheme). By default this will create Elliptic Curve keys.

### Asymmetric Key Generation
~~~
KeyPairHelper helper = new KeyPairHelper();
KeyPair p384KeyPair = helper.generateKeyPair(EllipticCurve.P384);
~~~

## Diffie-Hellman Key Exchange
The SDK can compute the shared secret for a Diffie-Hellman key exchange. By default it will do ECDH.

~~~
KeyAgreementHelper helper = new KeyAgreementHelper();
byte[] sharedSecretBytes = helper.generateSharedSecret(privateKey, publicKey);
~~~

## HMAC
The SDK will perform HMAC operations to ensure data integrity. By default it will perform HMAC-SHA384.

### Key Generation
~~~
MacHelper helper = new MacHelper();
SecretKey key = helper.generateKey();
~~~

### Digest
~~~
MacHelper helper = new MacHelper();
byte[] digestBytes = helper.digest(secretKey, dataBytes);
~~~

### Digest Verify
~~~
MacHelper helper = new MacHelper();
boolean verified = helper.verify(secretKey, dataBytes, digestBytes);
~~~

## Signature
The SDK can perform signing operations: sign and verify. By default it will perform ECDSA signing with SHA384.

### Signing
~~~
SignatureHelper helper = new SignatureHelper();
byte[] signatureBytes = helper.sign(privateKey, dataBytes);
~~~

### Signature Verification
~~~
SignatureHelper helper = new SignatureHelper();
byte[] signatureBytes = helper.verify(publicKey, dataBytes, signatureBytes);
~~~

## Elliptic Curve Cryptograpy Operations
The SDK has a helper class (ECCHelper) to perform all the basic ECC operations. This allows you to use a single helper to perform all ECC operations. See below for its usage. By default this will use curve P384 and AES256 for the IES encryption.

### Generate Key
~~~
ECCHelper helper = new ECCHelper();
KeyPair keyPair = helper.generateKey();
~~~

### ECDH Generate Shared Key
~~~
ECCHelper helper = new ECCHelper();
byte[] keyBytes = helper.generateDHSharedKey(privateKey, publicKey);
~~~

### ECDH Generate Shared Secret
~~~
ECCHelper helper = new ECCHelper();
byte[] secretBytes = helper.generateDHSharedSecret(privateKey, publicKey);
~~~

### Absio IES Encrypt
~~~
ECCHelper helper = new ECCHelper();
byte[] iesDataBytes = helper.absioIESEncrypt(plaintextBytes, signingPrivateKey, derivationPublicKey, senderId, objectId);
~~~

### IES Decrypt
~~~
ECCHelper helper = new ECCHelper();
byte[] plaintextBytes = helper.absioIESDecrypt(iesData, signingPublicKey, derivationPrivateKey);
~~~

### Sign
~~~
ECCHelper helper = new ECCHelper();
byte[] signatureBytes = helper.sign(privateKey, dataBytes);
~~~

### Verify Signature
~~~
ECCHelper helper = new ECCHelper();
byte[] signatureBytes = helper.verifySignature(publicKey, dataBytes, signatureBytes);
~~~

# SDK Resources

## API Documentation
You can find the [Java SDK API documentation](https://absio.github.io/java-sdk/) on GitHub.

## Library
You can find the platform independent [Java SDK](https://nexus.absio.com/#browse/browse:maven-releases:com%2Fabsio%2Fabsio-sdk) on the Absio Nexus.  If you would like a combined jar (with all dependencies) that is platfrom dependent) you can also pull those down from the Absio Nexus.

[32 bit combined Java SDK](https://nexus.absio.com/#browse/browse:maven-releases:com%2Fabsio%2Fabsio-sdk-win32)<br>
[64 bit combined Java SDK](https://nexus.absio.com/#browse/browse:maven-releases:com%2Fabsio%2Fabsio-sdk-win64)

# Support

## Support and Bug Reporting
**General Support and Feedback**

Please contact us at [support@absio.com](mailto:support@absio.com) if you experience any issues using this site, want to submit feedback, or have general questions about the technology.

**Bug Reporting**

Please use the relevant [GitHub](https://github.com/Absio/) Issue Tracker to report any bugs.
* [**Java SDK** - *https://github.com/Absio/java-sdk/issues*](https://github.com/Absio/java-sdk/issues)

# License
Visit the Absio documentation website to read the [Software License Agreement](http://docs.absio.com/#licenselicense).