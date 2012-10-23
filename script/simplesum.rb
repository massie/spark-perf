#!/usr/bin/env ruby

require "#{File.dirname(__FILE__) + "/../config/config.rb"}"
require "#{File.dirname(__FILE__) + "/common.rb"}"

if TESTS.include?("simplesum")

  results = RDD_SLICES.map do |args|
    puts "TEST, RDD_SLICES, TIME"
    result = run("spark.perf.SimpleSum", [args])
    puts result
    STDOUT.flush
    [args, result]
  end

end