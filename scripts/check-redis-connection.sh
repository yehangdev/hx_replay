#!/bin/bash
set -euo pipefail

REDIS_HOST="${REDIS_HOST:-cache.meituan.com}"
REDIS_PORT="${REDIS_PORT:-6379}"
REDIS_USER="${REDIS_USER:-admin}"
REDIS_PASSWORD="${REDIS_PASSWORD:-2a6b837c5790285f0544442049425419a8f0796ccdbab5cd}"

REDISCLI_AUTH="$REDIS_PASSWORD" redis-cli \
  -h "$REDIS_HOST" \
  -p "$REDIS_PORT" \
  --user "$REDIS_USER" \
  PING
