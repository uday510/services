#!/bin/bash

read -p "Docker Hub username: " username
read -s -p "Docker Hub token: " password
echo

base_dir=$(pwd)  # Save the root path (services/)
success_count=0
fail_count=0

for dir in accounts cards config-server eureka-server gateway-server; do
  image="${username}/${dir}:latest"
  echo "➡️  Building and pushing $image"

  if [ ! -d "$base_dir/$dir" ]; then
    echo "❌ Directory not found: $dir"
    ((fail_count++))
    continue
  fi

  cd "$base_dir/$dir" || { echo "❌ Cannot enter $dir"; ((fail_count++)); continue; }

  if [ ! -f "pom.xml" ]; then
    echo "❌ pom.xml not found in $dir"
    ((fail_count++))
    cd "$base_dir"
    continue
  fi

  echo "🔧 Building $dir..."
  mvn compile jib:build \
    "-Dimage=${image}" \
    "-Djib.to.auth.username=${username}" \
    "-Djib.to.auth.password=${password}"

  if [ $? -ne 0 ]; then
    echo "❌ Failed: $dir"
    ((fail_count++))
  else
    echo "✅ Success: $dir"
    ((success_count++))
  fi

  cd "$base_dir"  # Return to root after each build
done

echo "🏁 Build summary:"
echo "✅ $success_count successful"
echo "❌ $fail_count failed"

if [ $fail_count -gt 0 ]; then
  exit 1
fi