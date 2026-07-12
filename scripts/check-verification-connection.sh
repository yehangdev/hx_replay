#!/bin/bash
set -euo pipefail

REPLAY_HOST="${REPLAY_HOST:?REPLAY_HOST is required}"
REPLAY_PORT="${REPLAY_PORT:-9030}"
REPLAY_USER="${REPLAY_USER:?REPLAY_USER is required}"
REPLAY_PASSWORD="${REPLAY_PASSWORD:?REPLAY_PASSWORD is required}"
REPLAY_DB="${REPLAY_DB:-app_diancan_replay}"

export MYSQL_PWD="$REPLAY_PASSWORD"
mysql -h "$REPLAY_HOST" -P "$REPLAY_PORT" -u "$REPLAY_USER" "$REPLAY_DB" \
  -e "SELECT database() AS replay_db, now() AS server_time;"
unset MYSQL_PWD
