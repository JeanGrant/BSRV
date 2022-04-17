package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.Homepage.user_Profile;
import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccRatesBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createAcc_Rates extends Fragment {

    private FragmentCreateAccRatesBinding binding;
    private Boolean isConstRates = true;
    private List<Boolean> isRateEmpty = new ArrayList<>();
    private final Map<String,Long> rates = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccRatesBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) binding.btnProceed.getLayoutParams();
        initLstSubj();
        binding.inpTXTLayoutConstRates.setVisibility(View.VISIBLE);
        params.addRule(RelativeLayout.BELOW, R.id.inpTXTLayout_ConstRates);
        binding.btnProceed.setLayoutParams(params);

        binding.rdbConstRates.setOnClickListener(view -> {isConstRates = true; initLstSubj(); binding.inpTXTLayoutConstRates.setVisibility(View.VISIBLE); params.addRule(RelativeLayout.BELOW, R.id.inpTXTLayout_ConstRates); binding.btnProceed.setLayoutParams(params);});
        binding.rdbAltRates.setOnClickListener(view -> {isConstRates = false; initLstSubj(); binding.inpTXTLayoutConstRates.setVisibility(View.GONE); params.addRule(RelativeLayout.BELOW, R.id.layout_SubjRates); binding.btnProceed.setLayoutParams(params);});
        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Subjects()).commit());
        binding.btnProceed.setOnClickListener(view -> {

            ArrayList<String> subjects = Account_Details.User_Details.getSubjects();
            if(isConstRates) {
                for(int i=0; i<11; i++) {
                    isRateEmpty.add(String.valueOf(binding.inpTXTConstRates.getText()).isEmpty());
                }
            }
            if ((isConstRates&&!String.valueOf(binding.inpTXTConstRates.getText()).isEmpty()) || !isConstRates) {
                if (subjects.contains("Adobe Ps")) {
                    if (isConstRates) {
                        rates.put("Adobe Ps", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTAdobePs.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTAdobePs.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Adobe Ps", Long.parseLong(String.valueOf(binding.inpTXTAdobePs.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Adobe Ps", 0L);
                }
                if (subjects.contains("Animation")) {
                    if (isConstRates) {
                        rates.put("Animation", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTAnimation.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTAnimation.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Animation", Long.parseLong(String.valueOf(binding.inpTXTAnimation.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Animation", 0L);
                }
                if (subjects.contains("Arts")) {
                    if (isConstRates) {
                        rates.put("Arts", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTArts.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTArts.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Arts", Long.parseLong(String.valueOf(binding.inpTXTArts.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Arts", 0L);
                }
                if (subjects.contains("AutoCAD")) {
                    if (isConstRates) {
                        rates.put("AutoCAD", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTAutoCAD.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTAutoCAD.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("AutoCAD", Long.parseLong(String.valueOf(binding.inpTXTAutoCAD.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("AutoCAD", 0L);
                }
                if (subjects.contains("Engineering")) {
                    if (isConstRates) {
                        rates.put("Engineering", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTEngineering.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTEngineering.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Engineering", Long.parseLong(String.valueOf(binding.inpTXTEngineering.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Engineering", 0L);
                }
                if (subjects.contains("Languages")) {
                    if (isConstRates) {
                        rates.put("Languages", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTLanguages.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTLanguages.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Languages", Long.parseLong(String.valueOf(binding.inpTXTLanguages.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Languages", 0L);
                }
                if (subjects.contains("Law")) {
                    if (isConstRates) {
                        rates.put("Law", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTLaw.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTLaw.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Law", Long.parseLong(String.valueOf(binding.inpTXTLaw.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Law", 0L);
                }
                if (subjects.contains("MS Office")) {
                    if (isConstRates) {
                        rates.put("MS Office", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTMSOffice.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTMSOffice.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("MS Office", Long.parseLong(String.valueOf(binding.inpTXTMSOffice.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("MS Office", 0L);
                }
                if (subjects.contains("Mathematics")) {
                    if (isConstRates) {
                        rates.put("Mathematics", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTMathematics.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTMathematics.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Mathematics", Long.parseLong(String.valueOf(binding.inpTXTMathematics.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Mathematics", 0L);
                }
                if (subjects.contains("Programming")) {
                    if (isConstRates) {
                        rates.put("Programming", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTProgramming.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTProgramming.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Programming", Long.parseLong(String.valueOf(binding.inpTXTProgramming.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Programming", 0L);
                }
                if (subjects.contains("Sciences")) {
                    if (isConstRates) {
                        rates.put("Sciences", Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));
                    } else {
                        if (String.valueOf(binding.inpTXTSciences.getText()).isEmpty() || Long.parseLong(String.valueOf(binding.inpTXTSciences.getText()))==0) {
                            isRateEmpty.add(true);
                        } else {
                            rates.put("Sciences", Long.parseLong(String.valueOf(binding.inpTXTSciences.getText())));
                            isRateEmpty.add(false);
                        }
                    }
                } else {
                    rates.put("Sciences", 0L);
                }
            }

            Log.i("isRateEmpty", isRateEmpty.toString());
            Log.i("rates", rates.toString());

            if(!isRateEmpty.contains(true)){
                Account_Details.User_Details.setRates(rates);
                Log.i("rates", rates.toString());

                String currActivity = requireActivity().getClass().getCanonicalName();

                if(currActivity != null) {
                    if (currActivity.equals("com.example.mentor.Homepage.homepage")) {
                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                        DocumentReference df = fStore.collection("Users").document(Account_Details.User_Details.getUID());

                        Map<String,Object> updateInfo = new HashMap<>();
                        updateInfo.put("fullName", Account_Details.User_Details.getFullName());
                        updateInfo.put("bioEssay", Account_Details.User_Details.getBioEssay());
                        updateInfo.put("subjects", Account_Details.User_Details.subjects);
                        updateInfo.put("picString", Account_Details.User_Details.getPicString());
                        updateInfo.put("subjectRates", rates);

                        df.update(updateInfo);

                        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new user_Profile(), "user_Profile");
                    }else {
                        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Finalize(), "createAcc_Finalize");
                    }
                }
            }else{
                Toast.makeText(getContext(), "Kindly specify your hourly rate", Toast.LENGTH_SHORT).show();
            }
        });

        return viewLayout;
    }

    public void initLstSubj() {
        ArrayList<String> subjects = Account_Details.User_Details.subjects;
        if(subjects.contains("Adobe Ps")) {
            binding.layoutAdobePs.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTAdobePs.setVisibility(View.GONE);}
            else {
                binding.inpTXTAdobePs.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Adobe Ps")).equals("null")) {
                    binding.inpTXTAdobePs.setText(String.valueOf(Account_Details.User_Details.rates.get("Adobe Ps")));
                }
            }
        }
        if(subjects.contains("Animation")) {
            binding.layoutAnimation.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTAnimation.setVisibility(View.GONE);}
            else {
                binding.inpTXTAnimation.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Animation")).equals("null")) {
                    binding.inpTXTAnimation.setText(String.valueOf(Account_Details.User_Details.rates.get("Animation")));
                }
            }
        }
        if(subjects.contains("Arts")) {
            binding.layoutArts.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTArts.setVisibility(View.GONE);}
            else {
                binding.inpTXTArts.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Arts")).equals("null")) {
                    binding.inpTXTArts.setText(String.valueOf(Account_Details.User_Details.rates.get("Arts")));
                }
            }
        }
        if(subjects.contains("AutoCAD")) {
            binding.layoutAutoCAD.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTAutoCAD.setVisibility(View.GONE);}
            else {
                binding.inpTXTAutoCAD.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("AutoCAD")).equals("null")) {
                    binding.inpTXTAutoCAD.setText(String.valueOf(Account_Details.User_Details.rates.get("AutoCAD")));
                }
            }
        }
        if(subjects.contains("Engineering")) {
            binding.layoutEngineering.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTEngineering.setVisibility(View.GONE);}
            else {
                binding.inpTXTEngineering.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Engineering")).equals("null")) {
                    binding.inpTXTEngineering.setText(String.valueOf(Account_Details.User_Details.rates.get("Engineering")));
                }
            }
        }
        if(subjects.contains("Languages")) {
            binding.layoutLanguages.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTLanguages.setVisibility(View.GONE);}
            else {
                binding.inpTXTLanguages.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Languages")).equals("null")) {
                    binding.inpTXTLanguages.setText(String.valueOf(Account_Details.User_Details.rates.get("Languages")));
                }
            }
        }
        if(subjects.contains("Law")) {
            binding.layoutLaw.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTLaw.setVisibility(View.GONE);}
            else {
                binding.inpTXTLaw.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Law")).equals("null")) {
                    binding.inpTXTLaw.setText(String.valueOf(Account_Details.User_Details.rates.get("Law")));
                }
            }
        }
        if(subjects.contains("MS Office")) {
            binding.layoutMSOffice.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTMSOffice.setVisibility(View.GONE);}
            else {
                binding.inpTXTMSOffice.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("MS Office")).equals("null")) {
                    binding.inpTXTMSOffice.setText(String.valueOf(Account_Details.User_Details.rates.get("MS Office")));
                }
            }
        }
        if(subjects.contains("Mathematics")) {
            binding.layoutMathematics.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTMathematics.setVisibility(View.GONE);}
            else {
                binding.inpTXTMathematics.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Mathematics")).equals("null")) {
                    binding.inpTXTMathematics.setText(String.valueOf(Account_Details.User_Details.rates.get("Mathematics")));
                }
            }
        }
        if(subjects.contains("Programming")) {
            binding.layoutProgramming.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTProgramming.setVisibility(View.GONE);}
            else {
                binding.inpTXTProgramming.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Programming")).equals("null")) {
                    binding.inpTXTProgramming.setText(String.valueOf(Account_Details.User_Details.rates.get("Programming")));
                }
            }
        }
        if(subjects.contains("Sciences")) {
            binding.layoutSciences.setVisibility(View.VISIBLE);
            if (isConstRates) {binding.inpTXTSciences.setVisibility(View.GONE);}
            else {
                binding.inpTXTSciences.setVisibility(View.VISIBLE);
                if (!String.valueOf(Account_Details.User_Details.rates.get("Sciences")).equals("null")) {
                    binding.inpTXTSciences.setText(String.valueOf(Account_Details.User_Details.rates.get("Sciences")));
                }
            }
        }
    }
}