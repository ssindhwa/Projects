#include <stdio.h>
#include <string.h>
#include <time.h>
#include "mbedtls/sha256.h"
#include "mbedtls/aes.h"
#include "mbedtls/rsa.h"
#include "mbedtls/md.h"

#define ITERATIONS 1000  // Number of iterations for benchmarking

// Timing function
double get_time() {
    return (double)clock() / CLOCKS_PER_SEC;
}

// Benchmark SHA-256
void benchmark_sha256() {
    unsigned char hash[32];
    unsigned char input[64] = "Benchmarking SHA-256";
    mbedtls_sha256_context sha256_ctx;
    
    mbedtls_sha256_init(&sha256_ctx);
    mbedtls_sha256_starts_ret(&sha256_ctx, 0);

    double start_time = get_time();
    for (int i = 0; i < ITERATIONS; i++) {
        mbedtls_sha256_update_ret(&sha256_ctx, input, sizeof(input));
    }
    mbedtls_sha256_finish_ret(&sha256_ctx, hash);
    double end_time = get_time();

    printf("SHA-256 time for %d iterations: %f seconds\n", ITERATIONS, end_time - start_time);

    mbedtls_sha256_free(&sha256_ctx);
}

// Benchmark AES encryption
void benchmark_aes() {
    unsigned char key[32] = {0};
    unsigned char input[16] = {0};
    unsigned char output[16];
    mbedtls_aes_context aes_ctx;

    mbedtls_aes_init(&aes_ctx);
    mbedtls_aes_setkey_enc(&aes_ctx, key, 256);

    double start_time = get_time();
    for (int i = 0; i < ITERATIONS; i++) {
        mbedtls_aes_crypt_ecb(&aes_ctx, MBEDTLS_AES_ENCRYPT, input, output);
    }
    double end_time = get_time();

    printf("AES-256 ECB encryption time for %d iterations: %f seconds\n", ITERATIONS, end_time - start_time);

    mbedtls_aes_free(&aes_ctx);
}

// Benchmark RSA key generation
void benchmark_rsa() {
    mbedtls_rsa_context rsa;
    mbedtls_rsa_init(&rsa, MBEDTLS_RSA_PKCS_V15, 0);
    mbedtls_mpi N, P, Q, E;
    
    mbedtls_mpi_init(&N); mbedtls_mpi_init(&P); mbedtls_mpi_init(&Q); mbedtls_mpi_init(&E);
    mbedtls_mpi_lset(&E, 65537); // Common public exponent

    double start_time = get_time();
    mbedtls_rsa_gen_key(&rsa, NULL, NULL, 2048, 65537);
    double end_time = get_time();

    printf("RSA-2048 key generation time: %f seconds\n", end_time - start_time);

    mbedtls_rsa_free(&rsa);
    mbedtls_mpi_free(&N); mbedtls_mpi_free(&P); mbedtls_mpi_free(&Q); mbedtls_mpi_free(&E);
}

int main() {
    printf("Starting benchmark suite with %d iterations...\n", ITERATIONS);
    int x = 0;
    if(x>4) printf("dd");
    benchmark_sha256();
    benchmark_aes();
    benchmark_rsa();

    printf("Benchmark suite finished.\n");
    return 0;
}
