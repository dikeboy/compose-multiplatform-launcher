//
//  NativeResponse.swift
//  iosApp
//
//  Created by dikeboy on 2025/5/16.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp
import Foundation
import UIKit

struct AppInfo: Codable{
    let name: String
    let icon: String
    
    enum CodingKeys: String, CodingKey {
        case name,icon
    }
}

class IOSNativeResponseProvider: NativeResponseProvider {
    func getInstallApp() -> String? {
        // Native Swift logic; for example, add 100 to the input
        return loadLimitedAppInfo()
      
    }
    
    func loadImage(named: String) -> UIImage? {
         return UIImage(named: named)
    }
    
    
    private var installedApps = [AppInfo]()
    
    private func loadLimitedAppInfo() ->String? {
       // 1. 能检测的已知应用
       let detectableApps = [
           ("Wifi", "App-Prefs:root=WIFI", "setting_test"),
           ("通用", "App-Prefs:root=Genera", "setting_test"),
           ("关于本机", "App-Prefs:root=General&path=About", "setting_test"),
           ("存储空间", "App-Prefs:root=General&path=STORAGE_MGMT", "setting_test"),
       ]
       

     
       
       // 检测可识别的应用
       for (name, scheme, iconName) in detectableApps {
           if let url = URL(string: "\(scheme)"), UIApplication.shared.canOpenURL(url) {
               installedApps.append(AppInfo(name: name,icon: iconName))
           }
       }
        let encoder = JSONEncoder()
        do {
            let jsonData = try encoder.encode(installedApps)
            return String(data: jsonData, encoding: .utf8)
        } catch {
            print("JSON转换失败: \(error.localizedDescription)")
            return nil
        }
   }
}

