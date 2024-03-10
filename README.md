# Implementación de Servidor y Cliente SSL en Java

En este tutorial, te mostraré cómo implementar un servidor y un cliente SSL en Java paso a paso. Utilizaremos tres códigos principales: `ServidorSSL.java`, `ClienteSSL.java` y `CryptoUtils.java`.

## Paso 1: Generación del Keystore

Antes de ejecutar los códigos del servidor y el cliente SSL, necesitamos generar un keystore que contenga el certificado y la clave privada del servidor.

keytool -genkeypair -alias serverKey -keyalg RSA -keystore serverKeystore.jks

## Paso 2: Exportación del Certificado del Servidor

Una vez generado el keystore, exportaremos el certificado del servidor para que el cliente pueda confiar en él.

keytool -export -keystore serverKeystore.jks -alias serverKey -file serverCert.cer

## Paso 3: Importación del Certificado del Servidor en el Truststore del Cliente

El cliente necesita importar el certificado del servidor en su truststore para establecer una conexión segura con él.

keytool -importcert -alias serverCert -file server_certificate.cer -keystore clientTruststore.jks

## Paso 4: Configuración y Ejecución del Servidor SSL

Ahora vamos a configurar y ejecutar el servidor SSL. Abordaremos esto en el código `ServidorSSL.java`.

## Paso 5: Configuración y Ejecución del Cliente SSL

Finalmente, configuraremos y ejecutaremos el cliente SSL. Esto se realizará en el código `ClienteSSL.java`.

## Descripción de los Códigos

### `ServidorSSL.java`

Este código implementa un servidor SSL que espera conexiones de clientes. Configura el keystore y el SSLContext para la comunicación segura. Cuando se establece una conexión, el servidor espera recibir un mensaje cifrado del cliente, lo descifra y lo muestra por consola.

### `ClienteSSL.java`

Este código implementa un cliente SSL que establece una conexión segura con el servidor. Configura el truststore para confiar en el certificado del servidor. Luego, envía un mensaje cifrado al servidor para que lo descifre y lo muestre por consola.

### `CryptoUtils.java`

Este archivo contiene métodos de utilidad para cifrar y descifrar mensajes utilizando RSA.
