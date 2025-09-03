#!/bin/bash

echo "🚀 Setting up Student Online Learning Platform..."

# Check prerequisites
command -v java >/dev/null 2>&1 || { echo "❌ Java 17+ is required but not installed. Aborting." >&2; exit 1; }
command -v node >/dev/null 2>&1 || { echo "❌ Node.js 16+ is required but not installed. Aborting." >&2; exit 1; }
command -v mongod >/dev/null 2>&1 || { echo "⚠️  MongoDB not found. Please install MongoDB or use Docker." >&2; }

echo "✅ Prerequisites check completed"

# Setup Backend
echo "📦 Setting up backend dependencies..."
cd backend
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    echo "✅ Backend setup completed successfully"
else
    echo "❌ Backend setup failed"
    exit 1
fi

# Setup Frontend
echo "📦 Setting up frontend dependencies..."
cd ../frontend
npm install
if [ $? -eq 0 ]; then
    echo "✅ Frontend setup completed successfully"
else
    echo "❌ Frontend setup failed"
    exit 1
fi

cd ..

echo "🎉 Setup completed successfully!"
echo ""
echo "🔧 To start the application:"
echo "1. Start MongoDB: mongod --port 27017"
echo "2. Start backend: cd backend && mvn spring-boot:run"
echo "3. Start frontend: cd frontend && npm start"
echo ""
echo "🌐 Access the application at: http://localhost:3000"
echo "📡 API documentation at: http://localhost:8080/api"
echo ""
echo "📚 For more information, see README.md"
