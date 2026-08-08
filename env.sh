#!/bin/bash

if [ ! -f .env ]; then
    echo "❌ Error: .env file not found"
    return 1
fi

echo "🔄 Loading environment variables from .env..."

while IFS= read -r line || [ -n "$line" ]; do
    if [[ "$line" =~ ^[[:space:]]*# ]] || [[ -z "$line" ]]; then
        continue
    fi
    
    export "$line"
    
    var_name="${line%%=*}"
    var_value="${line#*=}"
    
    if [[ "$var_name" == *"PASSWORD"* ]] || [[ "$var_name" == *"SECRET"* ]]; then
        echo "    export $var_name=********"
    else
        echo "    export $var_name=$var_value"
    fi
    
done < .env

echo ""
echo "  Environment variables loaded successfully!"
echo "   Database: $POSTGRES_DB"
echo "   DB User: $POSTGRES_USER"
echo "   JWT Secret: ********"