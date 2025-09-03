import React from 'react';
import { useParams } from 'react-router-dom';

const CourseDetail = () => {
  const { id } = useParams();

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="px-6 py-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-4">
              Course Details
            </h1>
            <p className="text-gray-600">
              Course ID: {id}
            </p>
            <p className="mt-4 text-gray-500">
              Course details and enrollment functionality will be implemented here.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CourseDetail;