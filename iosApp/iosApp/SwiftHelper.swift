// iosApp/iosApp/SwiftHelper.swift
import Foundation

@objc public class SwiftHelper: NSObject {
    @objc public static func callSwiftMethod(_ param: String) -> String {
        return "Swift received: \(param)"
    }
}