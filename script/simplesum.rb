#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"

if TESTS.include?("simplesum")

  results = RDD_SLICES.product(NUM_REDUCE_TASKS).map do |args|
    result = `#{File.dirname(__FILE__) + "/run.rb spark.perf.SimpleSum " + args.join(" ")}`
    puts "SimpleSum,#{args.join(",")},#{result}"
    [args, result]
  end

end