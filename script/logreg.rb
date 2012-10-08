#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"

if TESTS.include?("logreg")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS, LR_NUM_POINTS, LR_DIMENSION).map do |args|
    result = `#{File.dirname(__FILE__) + "/run.rb spark.perf.LogReg " + args.join(" ")}`
    puts "LogReg,#{args.join(",")},#{result}"
    [args, result]
  end
  
end

