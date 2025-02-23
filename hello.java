import React, { useState, useEffect } from "react";
import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import axios from "axios";

const mockData = [
  { day: "Mon", steps: 8000 },
  { day: "Tue", steps: 7500 },
  { day: "Wed", steps: 9000 },
  { day: "Thu", steps: 10000 },
  { day: "Fri", steps: 9500 },
  { day: "Sat", steps: 11000 },
  { day: "Sun", steps: 5000 },
];

export default function FitnessDashboard() {
  const [stepsData, setStepsData] = useState(mockData);
  const [workout, setWorkout] = useState("");
  const [calories, setCalories] = useState(0);
  const [user, setUser] = useState(null);
  const [waterIntake, setWaterIntake] = useState(0);
  const [sleepHours, setSleepHours] = useState(0);

  useEffect(() => {
    axios.get("/api/user").then(response => setUser(response.data));
    const avgSteps = stepsData.reduce((sum, d) => sum + d.steps, 0) / stepsData.length;
    if (avgSteps < 8000) setWorkout("Try a 30-minute cardio session!");
    else if (avgSteps < 10000) setWorkout("Include strength training today!");
    else setWorkout("Great job! Keep up the routine.");
    setCalories(avgSteps * 0.04);
    setWaterIntake((avgSteps / 1000) * 0.5);
    setSleepHours(8 - (avgSteps / 10000));
  }, [stepsData]);

  return (
    <div className="p-6 space-y-6 bg-gradient-to-r from-blue-900 via-purple-900 to-black min-h-screen text-white">
      <h1 className="text-2xl font-bold">Fitness Tracker Dashboard</h1>
      {user && <p>Welcome, {user.name}!</p>}
      <Card className="bg-gray-800 text-white">
        <CardContent>
          <h2 className="text-xl font-semibold">Daily Steps</h2>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={stepsData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#ffffff" />
              <XAxis dataKey="day" stroke="#ffffff" />
              <YAxis stroke="#ffffff" />
              <Tooltip />
              <Line type="monotone" dataKey="steps" stroke="#4CAF50" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
      <Card className="bg-gray-800 text-white">
        <CardContent>
          <h2 className="text-lg font-semibold">Workout Recommendation</h2>
          <p>{workout}</p>
        </CardContent>
      </Card>
      <Card className="bg-gray-800 text-white">
        <CardContent>
          <h2 className="text-lg font-semibold">Calories Burned</h2>
          <p>{calories.toFixed(2)} kcal</p>
        </CardContent>
      </Card>
      <Card className="bg-gray-800 text-white">
        <CardContent>
          <h2 className="text-lg font-semibold">Water Intake Recommendation</h2>
          <p>Drink at least {waterIntake.toFixed(2)} liters of water today</p>
        </CardContent>
      </Card>
      <Card className="bg-gray-800 text-white">
        <CardContent>
          <h2 className="text-lg font-semibold">Recommended Sleep Hours</h2>
          <p>Try to get {sleepHours.toFixed(2)} hours of sleep tonight</p>
        </CardContent>
      </Card>
      <Button className="text-white" onClick={() => alert("More features coming soon!")}>Explore More</Button>
    </div>
  );
}

